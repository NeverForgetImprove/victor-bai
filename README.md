# victor-bai
Never 
==
Never forget Improve
--

### .gitignore rules:<br>
>*.a&emsp;&emsp;&emsp;&emsp;忽略所有 .a 结尾的文件<br>
>!lib.a&emsp;&emsp;&emsp;但 lib.a 除外<br>
>/TODO&emsp;&emsp;仅仅忽略项目根目录下的 TODO 文件<br>
>build/&emsp;&emsp;&ensp;忽略 build/ 目录下的所有文件<br>

Tip: .gitignore 只能忽略那些原来没有被track的文件，如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的。<br>
那么解决方法就是先把本地缓存删除（改变成未track状态），然后再提交：<br>

    git rm -r --cached .
    git add .
