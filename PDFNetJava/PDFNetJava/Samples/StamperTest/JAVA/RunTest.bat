@echo off
setlocal
set TEST_NAME=StamperTest
javac.exe -cp .;../../../Lib/PDFNet.jar *.java
java.exe -Djava.library.path=../../../Lib -classpath .;../../../Lib/PDFNet.jar %TEST_NAME%
endlocal
