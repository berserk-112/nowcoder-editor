# 自定义生成代码设置过程

- 进入到nowcoder的设置界面,可通过直接点击nowcoder界面中的小齿轮图标进入，或者通过IDEA settings->Tools->nowcoder Plugin进入

- 勾选Custom Template

- 按相应的需求设置Code FileName, Code Template

![custom code.png](https://s3.bmp.ovh/imgs/2023/02/20/03e1c10c2c76e1b5.png)

## 说明
勾选自定代码后，会造成部分题目无法打开，这是因为牛客网中的题目并没有英文标题。question.titleSlug实际是使用的题目模板代码中的方法名，当多个题目名称相同时会出现题目无法打开的情况，暂时的解决方法是手动修改重名的文件。
## 推荐模板格式

- Code FileName:
```(java)
$!velocityTool.camelCaseName(${question.titleSlug})
```

- Code Template:

```(java)
${question.content}
package nowcoder.editor.cn;  //根据实际修改
${question.code}
    public static void main(String[] args) {
        $!velocityTool.camelCaseName(${question.titleSlug}) solution = new $!velocityTool.camelCaseName(${question.titleSlug})();
    }
}
```

