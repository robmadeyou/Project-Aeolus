@echo off
title RuneScape
:run
CLS
java -Xmx1000m -cp bin;deps/gson-2.2.4.jar;deps/guava-18.0.jar;deps/jython.jar;deps/log4j-1.2.15.jar;deps/mina.jar;deps/mysql.jar;deps/netty.jar;deps/poi.jar;deps/slf4j.jar;slf4j-nop.jar;deps/xstream.jar; core.game.Server
goto run
pause