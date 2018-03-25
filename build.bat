@echo off
cls
call gradlew.bat clean build -x buildAngular -x installAngular -x findbugsMain -x findbugsTest -x pmdMain -x pmdTest
pause