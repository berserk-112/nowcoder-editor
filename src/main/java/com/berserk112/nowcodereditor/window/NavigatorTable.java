package com.berserk112.nowcodereditor.window;

import com.berserk112.nowcodereditor.listener.JTableKeyAdapter;
import com.berserk112.nowcodereditor.listener.QuestionStatusListener;
import com.berserk112.nowcodereditor.listener.TreeMouseListener;
import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.PageInfo;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.setting.PersistentConfig;
import com.berserk112.nowcodereditor.utils.PropertiesUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import icons.NowCoderEditorIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuzijun
 */
public class NavigatorTable extends JPanel {

    private static Color Level1 = new Color(153, 153, 153);
    private static Color Level2 = new Color(59, 151, 255);
    private static Color Level3 = new Color(50, 202, 153);

    private static Color Level4 = new Color(255, 170, 32);

    private static Color Level5 = new Color(255, 86, 28);
    private static Color defColor = null;

    private boolean first = true;

    private JBTable table;
    private MyTableModel tableModel;
    private Project project;

    private List<Question> questionList;
    private JComboBox page;
    private PageInfo<Question> pageInfo = new PageInfo<>(1, 50);


    public NavigatorTable(Project project) {
        super(new BorderLayout());
        this.project = project;
        loaColor();
        tableModel = new MyTableModel();
        table = new JBTable(tableModel) {
            @Override
            public String getToolTipText(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                String tipTextString = null;
                if (row > -1 && col == 1) {
                    Object value = table.getValueAt(row, col);
                    if (null != value && !"".equals(value))
                        tipTextString = value.toString();
                }
                return tipTextString;
            }

            @Override
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        return MyTableModel.columnName[realIndex];
                    }
                };
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                if (first) {
                    if (row == 0 && column == 0) {
                        return firstToolTip();
                    } else {
                        return super.prepareRenderer(renderer, row, column);
                    }
                }
                Component component = super.prepareRenderer(renderer, row, column);
                if (defColor == null) {
                    synchronized (NavigatorTable.class) {
                        if (defColor == null) {
                            defColor = component.getForeground();
                        }
                    }
                }
                DefaultTableModel model = (DefaultTableModel) this.getModel();
                if (column == 3) {
                    Object value = model.getValueAt(row, column);
                    if (value != null) {
                        if (value.toString().equals("入门")) {
                            component.setForeground(Level1);
                        } else if (value.toString().equals("简单")) {
                            component.setForeground(Level2);
                        } else if (value.toString().equals("中等")) {
                            component.setForeground(Level3);
                        } else if (value.toString().equals("较难")) {
                            component.setForeground(Level4);
                        } else if (value.toString().equals("困难")) {
                            component.setForeground(Level5);
                        }
                    } else {
                        component.setForeground(defColor);
                    }
                } else {
                    component.setForeground(defColor);
                }
                return component;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setFillsViewportHeight(true);
        table.addMouseListener(new TreeMouseListener(this, project));
        table.addKeyListener(new JTableKeyAdapter(this, project));
        table.setRowHeight(0, 200);

        this.add(new JBScrollPane(table, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        this.add(paging(), BorderLayout.SOUTH);

        project.getMessageBus().connect().subscribe(QuestionStatusListener.QUESTION_STATUS_TOPIC, new QuestionStatusListener() {
            @Override
            public void updateTable(Question question) {
                if (questionList != null) {
                    for (Question q : questionList) {
                        if (q.getTitle().equals(question.getTitle())) {
                            q.setStatus(question.getStatus());
                            refreshData();
                            break;
                        }
                    }
                }
            }
        });
    }

    public static void loaColor() {
        Config config = PersistentConfig.getInstance().getInitConfig();
        if (config != null) {
            Color[] colors = config.getFormatLevelColour();
            Level1 = colors[0];
            Level2 = colors[1];
            Level3 = colors[2];
            Level4 = colors[3];
            Level5 = colors[4];
        }
    }

    private Component paging() {
        JPanel paging = new JPanel(new BorderLayout());
        Integer[] pageSizeData = {20, 50, 100};
        JComboBox pageSizeBox = new ComboBox(pageSizeData);
        pageSizeBox.setPreferredSize(new Dimension(60, -1));
        pageSizeBox.setSelectedItem(50);
        pageSizeBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                pageInfo.setPageSize((Integer) e.getItem());
            }
        });
        paging.add(pageSizeBox, BorderLayout.WEST);

        JPanel control = new JPanel(new BorderLayout());
        JButton previous = new JButton("<");
        previous.setToolTipText("Previous");
        previous.setPreferredSize(new Dimension(50, -1));
        previous.setMaximumSize(new Dimension(50, -1));
        previous.addActionListener(event -> {
            if (page.getItemCount() <= 0 || (int) page.getSelectedItem() < 2) {
                return;
            } else {
                pageInfo.setPageIndex((int) page.getSelectedItem() - 1);
                NavigatorTable pNavigatorTable = this;
                ProgressManager.getInstance().run(new Task.Backgroundable(project, "Previous", false) {
                    @Override
                    public void run(@NotNull ProgressIndicator progressIndicator) {
                        ViewManager.loadServiceData(pNavigatorTable, project);
                    }
                });
            }

        });
        control.add(previous, BorderLayout.WEST);
        JButton next = new JButton(">");
        next.setToolTipText("Next");
        next.setPreferredSize(new Dimension(50, -1));
        next.setMaximumSize(new Dimension(50, -1));
        next.addActionListener(event -> {
            if (page.getItemCount() <= 0 || (int) page.getSelectedItem() >= page.getItemCount()) {
                return;
            } else {
                pageInfo.setPageIndex((int) page.getSelectedItem() + 1);
                NavigatorTable pNavigatorTable = this;
                ProgressManager.getInstance().run(new Task.Backgroundable(project, "Next", false) {
                    @Override
                    public void run(@NotNull ProgressIndicator progressIndicator) {
                        ViewManager.loadServiceData(pNavigatorTable, project);
                    }
                });
            }

        });
        control.add(next, BorderLayout.EAST);
        page = new ComboBox();
        control.add(page, BorderLayout.CENTER);
        paging.add(control, BorderLayout.CENTER);

        JButton go = new JButton("Go");
        go.setPreferredSize(new Dimension(50, -1));
        go.setMaximumSize(new Dimension(50, -1));
        go.addActionListener(event -> {
            if (page.getItemCount() <= 0) {
                return;
            } else {
                pageInfo.setPageIndex((int) page.getSelectedItem());
                NavigatorTable pNavigatorTable = this;
                ProgressManager.getInstance().run(new Task.Backgroundable(project, "Go to", false) {
                    @Override
                    public void run(@NotNull ProgressIndicator progressIndicator) {
                        ViewManager.loadServiceData(pNavigatorTable, project);
                    }
                });
            }

        });
        paging.add(go, BorderLayout.EAST);

        return paging;
    }

    public int getPageIndex() {
        if (page.getItemCount() <= 0) {
            return 1;
        } else {
            return (int) page.getSelectedItem();
        }
    }

    public PageInfo<Question> getPageInfo() {
        return pageInfo;
    }

    public void refreshData() {
        ApplicationManager.getApplication().invokeLater(() -> {
            this.tableModel.updateData(questionList);
            setColumnWidth();
        });
    }

    public void loadData(PageInfo<Question> pageInfo) {
        ApplicationManager.getApplication().invokeLater(() -> {
            if (this.first) {
                this.tableModel.setRowCount(0);
                this.tableModel.setColumnCount(5);
                this.first = false;
            }
            this.questionList = pageInfo.getRows();
            this.tableModel.updateData(questionList);
            setColumnWidth();
        });
        if (pageInfo.getPageTotal() != this.page.getItemCount()) {
            this.page.removeAllItems();
            for (int i = 1; i <= pageInfo.getPageTotal(); i++) {
                this.page.addItem(i);
            }
        }
        this.page.setSelectedItem(pageInfo.getPageIndex());
        this.pageInfo = pageInfo;
    }

    public Question getSelectedRowData() {
        int row = table.getSelectedRow();
        if (row < 0 || questionList == null || row >= questionList.size()) {
            return null;
        }
        return questionList.get(row);
    }

    private void setColumnWidth() {
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(2).setMaxWidth(70);
        table.getColumnModel().getColumn(3).setMaxWidth(60);
        table.getColumnModel().getColumn(4).setMaxWidth(50);
    }

    private JTextPane firstToolTip() {
        JTextPane myPane = new JTextPane();
        myPane.setOpaque(false);
        String addIconText = "'login'";
        String refreshIconText = "'refresh'";
        String configIconText = "'config'";
        String message = PropertiesUtils.getInfo("config.load", addIconText, refreshIconText, configIconText);
        int addIconMarkerIndex = message.indexOf(addIconText);
        myPane.replaceSelection(message.substring(0, addIconMarkerIndex));
        myPane.insertIcon(NowCoderEditorIcons.LOGIN);
        int refreshIconMarkerIndex = message.indexOf(refreshIconText);
        myPane.replaceSelection(message.substring(addIconMarkerIndex + addIconText.length(), refreshIconMarkerIndex));
        myPane.insertIcon(NowCoderEditorIcons.REFRESH);
        int configIconMarkerIndex = message.indexOf(configIconText);
        myPane.replaceSelection(message.substring(refreshIconMarkerIndex + refreshIconText.length(), configIconMarkerIndex));
        myPane.insertIcon(NowCoderEditorIcons.CONFIG);
        myPane.replaceSelection(message.substring(configIconMarkerIndex + configIconText.length()));
        return myPane;
    }

    private static class MyTableModel extends DefaultTableModel {

        private NumberFormat nf = NumberFormat.getPercentInstance();

        public static String[] columnName = {"Status", "Title", "Acceptance", "Difficulty", "Frequency"};
        private static String[] columnNameShort = {"STAT", "Title", "AC", "DD", "F"};

        public MyTableModel() {
            super(new Object[]{"info"}, 1);
            nf.setMinimumFractionDigits(1);
            nf.setMaximumFractionDigits(1);
        }

        public Object getValue(Question question, int columnIndex) {
            if (columnIndex == 0) {
                return question.getStatusSign();
            }
            if (columnIndex == 1) {
                return question.getFormTitle();
            }

            if (columnIndex == 2) {
                return nf.format(question.getAcceptRate() * 0.01);
            }

            if (columnIndex == 3) {
                Integer level = question.getLevel();
                if (level == 1) {
                    return "入门";
                } else if (level == 2) {
                    return "简单";
                } else if (level == 3) {
                    return "中等";
                } else if (level == 4) {
                    return "较难";
                } else if (level == 5) {
                return "困难";
                } else {
                    return level;
                }
            }
            if (columnIndex == 4) {
                return question.getPostCount() == null ? 0 : question.getPostCount();
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public void updateData(List<Question> questionList) {
            if (questionList == null) {
                questionList = new ArrayList<>();
            }
            Object[][] dataVector = new Object[questionList.size()][columnName.length];
            for (int i = 0; i < questionList.size(); i++) {
                Object[] line = new Object[columnName.length];
                for (int j = 0; j < columnName.length; j++) {
                    line[j] = getValue(questionList.get(i), j);
                }
                dataVector[i] = line;
            }
            setDataVector(dataVector, MyTableModel.columnNameShort);
        }
    }
}
