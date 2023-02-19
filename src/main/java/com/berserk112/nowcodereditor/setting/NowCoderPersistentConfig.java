package com.berserk112.nowcodereditor.setting;

import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.PluginConstant;
import com.berserk112.nowcodereditor.utils.MessageUtils;
import com.berserk112.nowcodereditor.utils.PropertiesUtils;
import com.berserk112.nowcodereditor.model.Constant;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author shuzijun
 */
@State(name = "NowCoderPersistentConfig" + PluginConstant.ACTION_SUFFIX, storages = {@Storage(value = PluginConstant.ACTION_PREFIX + "-config.xml", roamingType = RoamingType.DISABLED)})
public class NowCoderPersistentConfig implements PersistentStateComponent<NowCoderPersistentConfig> {

    public static String PATH = "nowcoder" + File.separator + "editor";
    public static String OLDPATH = "nowcoder-plugin";
    private static String INITNAME = "initConfig";

    private Map<String, Config> initConfig = new HashMap<>();


    @Nullable
    public static NowCoderPersistentConfig getInstance() {
        return ServiceManager.getService(NowCoderPersistentConfig.class);
    }

    @Nullable
    @Override
    public NowCoderPersistentConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull NowCoderPersistentConfig nowCoderPersistentConfig) {
        XmlSerializerUtil.copyBean(nowCoderPersistentConfig, this);
    }

    public Config getInitConfig() {
        Config config = initConfig.get(INITNAME);
        if (config != null && config.getVersion() != null && config.getVersion() < Constant.PLUGIN_CONFIG_VERSION_1) {
            Iterator<String> iterator = config.getUserCookie().keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = config.getCookie(key);
                if (StringUtils.isBlank(value)) {
                    iterator.remove();
                }
            }
            config.setVersion(Constant.PLUGIN_CONFIG_VERSION_1);
            setInitConfig(config);
        }
        return config;
    }

    public Config getConfig() {
        Config config = getInitConfig();
        if (config == null) {
            MessageUtils.showAllWarnMsg("warning", PropertiesUtils.getInfo("config.first"));
            throw new UnsupportedOperationException("not configured:File -> settings->tools->nowcoder plugin");
        } else {
            return config;
        }

    }

    public void setInitConfig(Config config) {
        initConfig.put(INITNAME, config);
    }

    public String getTempFilePath() {
        return getConfig().getFilePath() + File.separator + PATH + File.separator + initConfig.get(INITNAME).getAlias() + File.separator;
    }

    public void savePassword(String password, String username) {
        if (username == null || password == null) {
            return;
        }
        PasswordSafe.getInstance().set(new CredentialAttributes(PluginConstant.PLUGIN_ID, username, this.getClass()), new Credentials(username, password == null ? "" : password));
    }

    public String getPassword(String username) {
        if (getConfig().getVersion() != null && username != null) {
            return PasswordSafe.getInstance().getPassword(new CredentialAttributes(PluginConstant.PLUGIN_ID, username, this.getClass()));
        }
        return null;

    }

}
