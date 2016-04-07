##SynTree

A Syntax Tree Tool based on the JGraph library.


###2016-04-07 What's new: 

1.The application has been localized in English, French, Japanese.

2.A new Preferences Dialog allows the user to customize colors, layout, and look.

3.Data are automatically backed up when the application exits.

4.A contextual Edition menu has been added.

5.Part of the sentence can be highlighted while clicking on a box.

6.The toolbar layout has been improved and Zoom in/Zoom out buttons have been added.

7.Implemented rectangle area multi-selection.

8.Added build.xml file to easily build the application using Ant.


###2013-12-01 What's new: 

1.修复由于文件中不存在该句法树而造成软件卡死的情况；

2.修复中英文句子数量不对照时，无法读取较多数量文件的情况；

3.增加刷新功能可直接从文件中重新读取文件；

4.增加文件直接拖入显示的功能；

5.增加显示分词序数的功能。

###2013-10-21 What's new: 

1.修改生成语法树顺序的保存方式，改为仅与叶子结点坐标相关，与其父结点的坐标顺序不再相关；

2.修改错误提示的方式，直接将错误结点的背景改为红色。



###2013-10-17 What's new: 

1.修复由于连线父节点端没有连好造成结点丢失的情况。



###2013-10-13 What's new: 

1.修复选择文件路径后没有自动刷新的Bug；

2.修改工具使用过程中树的保存方法：先将校对过的树保存在哈希表里，最       后统一存储提高运行效率；

3.修正整理功能树的中间保存方式；

4.对括号对表示的树的格式要求降低，提高兼容性；

5.修正当中英文文本句子数不一致情况下软件抛出的异常。




###2013-10-8 What's new:

1.修正结点子节点按照坐标顺序保存句法树的括号对形式；

2.添加对句法树修改工具中出现孤立结点的判断；

3.当出现孤立结点及多个根节点时提示结点名称;

4.修正使用工具时从第二节开始的BUG。



###2013-09-28 What's new: 

1.新增文件路径自定义功能；

2.新增跳转前询问用户是否保存；

3.修复点击下一个、上一个仅保存当前汉语/英语语法树的Bug；

4.修复句法树从0开始计数的Bug。



###2013-09-24 What's new: 

1.全新的界面布局，采用beauty eye风格界面，显示效果更清晰；

2.添加菜单栏、工具栏，操作更方便；

3.新增撤销、重做功能，避免修改过程误操作；

4.新增语法树整理功能，可是实时查看修改效果，更加方便快捷；

5.新增显示句子功能，并可以切换显示句子、显示分词、显示分词及词性；

6.新增显示当前所操作句子位置，并增加直接跳转选择句子位置功能；

7.新增中英文句法树对照显示功能；

8.新增修改过程的非法操作的判断；

9.修复保存结果的递归算法，改用自己编写的模块更适合所处理的语料库。
