# victor-bai
Never Never forget Improve

.gitignore rules:
    *.a             忽略所有 .a 结尾的文件
    !lib.a          但 lib.a 除外
    /TODO         仅仅忽略项目根目录下的 TODO 文件
    build/          忽略 build/ 目录下的所有文件
Tip: .gitignore 只能忽略那些原来没有被track的文件，如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的。
那么解决方法就是先把本地缓存删除（改变成未track状态），然后再提交：
    git rm -r --cached .
    git add .
