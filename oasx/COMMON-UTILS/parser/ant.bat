@echo off
set ANT_HOME=.
set CP=.;%JAVA_HOME%/lib/tools.jar;lib/ant.jar;lib/ant-launcher.jar;lib/junit-3.8.1.jar;lib/ant-junit.jar
echo classpath %CP%
java -cp %CP% org.apache.tools.ant.Main %*




