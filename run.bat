rem home pc
rem set JAVA_HOME=g:\Program Files\Java\jdk1.6.0_27\
rem laptop
rem set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_23
rem company
set JAVA_HOME=d:\Program Files\Java\jdk1.6.0_31


set path=%JAVA_HOME%\bin;path
set classpath=bin;%JAVA_HOME%\lib;lib/commons-dbcp-1.2.2.jar;lib/commons-logging.jar;lib/commons-pool-1.4.jar;lib/spring.jar;lib/commons-beanutils.jar;lib\jaybird-full-2.1.6.jar;lib/mybatis-3.0.2.jar;lib/MyLib.jar;lib/commons-lang-2.5.jar;lib/commons-collections-3.2.1.jar;lib/commons-configuration-1.7.jar;..\Bird_Common_Lib\bin;
java -splash:lib/splash.png frame.FrmMainI
pause