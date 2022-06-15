package com.berserk112.nowcodereditor.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuzijun
 */
public enum CodeTypeEnum {
    JAVA("Java", "java", ".java", "//", "/**\n%s\n*/", 4),
    PYTHON("Python", "python", ".py", "# ","\"\"\"\n%s\n\"\"\"", 5),
    CPP("C++", "c", ".cpp", "//", "/**\n%s\n*/", 2),
    PYTHON3("Python3", "python3", ".py", "# ","\"\"\"\n%s\n\"\"\"", 11),
    C("C", "purec", ".c", "//", "/**\n%s\n*/", 1),
    CSHARP("C#", "cSharp", ".cs", "//", "/**\n%s\n*/", 9),
    JAVASCRIPT("JavaScript", "js", ".js", "//", "/**\n%s\n*/", 13),
    RUBY("Ruby", "ruby", ".rb", "#","=begin\n%s\n=end", 19),
    SWIFT("Swift", "swift", ".swift", "///", "/**\n%s\n*/", 20),
    GO("Go", "go", ".go", "//", "/**\n%s\n*/", 17),
    SCALA("Scala", "scala", ".scala", "//", "/**\n%s\n*/", 28),
    KOTLIN("Kotlin", "kotlin", ".kt", "//", "/**\n%s\n*/", 29),
    RUST("Rust", "rust", ".rs", "//", "/**\n%s\n*/", 27),
    PHP("PHP", "php", ".php", "//", "/**\n%s\n*/", 8),
    BASH("Bash", "bash", ".sh", "#",": '\n%s\n'", 23),
    MYSQL("MySQL", "mysql", ".sql", "#", "/**\n%s\n*/", 32),
//    ORACLE("Oracle", "oraclesql", ".sql", "#", "/**\n%s\n*/"),
//    MSSQLSERVER("MS SQL Server", "mssql", ".sql", "#", "/**\n%s\n*/"),
    TypeScript("TypeScript", "ts", ".ts", "//", "/**\n%s\n*/", 31),
    GROOVY("Groovy", "grooovy", ".groovy", "//", "/**\n%s\n*/", 30)
    ;


    private String type;
    private String langSlug;
    private String suffix;
    private String comment;
    private String multiLineComment;
    private Integer langId;

    CodeTypeEnum(String type, String langSlug, String suffix, String comment, String multiLineComment, Integer langId) {
        this.type = type;
        this.langSlug = langSlug;
        this.suffix = suffix;
        this.comment = comment;
        this.multiLineComment = multiLineComment;
        this.langId = langId;
    }

    private static Map<String, CodeTypeEnum> MAP = new HashMap<String, CodeTypeEnum>();
    private static Map<String, CodeTypeEnum> LANGSLUGMAP = new HashMap<String, CodeTypeEnum>();

    static {
        for (CodeTypeEnum c : CodeTypeEnum.values()) {
            MAP.put(c.getType().toUpperCase(), c);
            LANGSLUGMAP.put(c.langSlug.toUpperCase(), c);
        }
    }

    public void setLangId(Integer langId) {
        this.langId = langId;
    }

    public String getType() {
        return type;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getLangSlug() {
        return langSlug;
    }

    public Integer getLangId() {
        return langId;
    }
    public static CodeTypeEnum getCodeTypeEnum(String type) {
        return MAP.get(type.toUpperCase());
    }

    public static CodeTypeEnum getCodeTypeEnumByLangSlug(String langSlug) {
        return LANGSLUGMAP.get(langSlug.toUpperCase());
    }

    public String getComment() {
        return comment;
    }

    public String getMultiLineComment() {
        return multiLineComment;
    }
}
