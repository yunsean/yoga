<#import "/macro_common.ftl" as macroCommon>
<!DOCTYPE html>
<html>
<@macroCommon.html/>
<frameset rows="40,*" frameborder="no" border="0" framespacing="0">
    <frame src="/admin/top" name="userLogFrame" scrolling="No" noresize="noresize" id="userLogFrame" title="userLogFrame" />
    <frameset cols="160,*" frameborder="no" border="0" framespacing="0">
	    <frame src="/admin/left" name="sidebar" scrolling="yes" noresize="noresize" id="sidebar" title="sidebar" />
	    <frame src="/admin/welcome" name="mainFrame" id="mainFrame" title="mainFrame" />
    </frameset>
  </frameset>
<noframes>
	<body>您的浏览器不支持框架！</body>
</noframes>
</html>