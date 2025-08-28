@echo off
echo [DEBUG] ARGS: %*
setlocal ENABLEDELAYEDEXPANSION

:: === Initialize
set "mountDir="
set "LANGUAGE="

@echo off
setlocal enabledelayedexpansion

goto GETOPTS

:Help
echo.
echo =============================
echo            # Help
echo =============================
echo Usage:
echo   runsonar.bat [options]
echo.
echo Options:
echo   -h or --help     Show this help message.
echo   -path            Path to the root of the project directory.
echo   -coveragePath    Relative path to the coverage report (e.g. \target\site).
echo   -projectKey      Key for publishing project to sonar server.
echo   -exclude         Glob pattern for files/folders to exclude from analysis.
echo   -include         Glob pattern for files/folders to include in analysis.
echo   -tags            Comma-separated project tags for categorization in SonarQube.
echo   -j               Force language to Java.
echo   -t               Force language to TypeScript.
echo   -p               Force language to Python.
echo.
echo Examples:
echo   runsonar.bat
echo   runsonar.bat -path D:\MyProject
echo   runsonar.bat -path D:\MyProject -j -coveragePath target\site
echo   runsonar.bat -path D:\MyProject -t -exclude "**/test/**" -tags "backend,legacy"
echo   FULL EXAMPLE:
echo   runsonar.bat -path D:\Projects\MyApp -j -coveragePath target\site -projectKey MyProjectKey -exclude "**/generated/**,**/wrapper/**" -include "src/main/**" -tags "finance,critical"
echo.
echo Please provide correct arguments. Default project detection is attempted if language is not specified.
goto :eof

:GETOPTS
if /I "%1" == "-h" call :Help & goto :eof

:LOOP
if /I "%1" == "-path" (
    set "mountDir=%2"
    shift
)
if /I "%1" == "-coveragePath" (
    set "coveragePath=%2"
    shift
)
if /I "%1" == "-projectKey" (
    set "projectKey=%2"
    shift
)
if /I "%1" == "-configPath" (
    set "configPath=%2"
    shift 
)
if /i "%~1"=="-exclude" (
    set "exclusions=%~2"
    if defined exclusions (
        set "exclusions=!exclusions:"=!"
    )
    shift
    shift
    goto LOOP
)
if /i "%~1"=="-include" (
    set "inclusions=%~2"
    if defined inclusions (
        set "inclusions=!inclusions:"=!"
    )
    shift
    shift
    goto LOOP
)
if /I "%1" == "-tags" (
    set "rawtags=%~2"
    call set "tags=%%~2"
    shift
)
if /I "%1" == "-j" (
    set "LANGUAGE=java"
)
if /I "%1" == "-t" (
    set "LANGUAGE=ts"
)
if /I "%1" == "-p" (
    set "LANGUAGE=python"
)

shift
if not "%1"=="" goto LOOP

::echo POINT 0
echo PARAMETER SET FOR SONAR RUN:
echo ===================================
echo MOUNT_DIR: %mountDir%
echo LANGUAGE: %LANGUAGE%
echo COVERAGE_PATH: %coveragePath%
echo PROJECT_KEY: %projectKey%
echo EXCLUSION REGEX: %exclusions%
echo INCLUSION REGEX: %inclusions%
echo PROJECT_TAGS: %tags%
echo ===================================

::echo POINT 1
:: === Default to current directory if mountDir not provided
if not defined mountDir (
    set "mountDir=%cd%"
	echo mountDir: %mountDir%
)

if not exist "%mountDir%" (
    echo [ERROR] Path does not exist: %mountDir%
    exit /b 1
)

::echo POINT 3
:: === Auto-detect language if not specified
if not defined LANGUAGE (
    echo [INFO] No language flag provided, trying to auto-detect language...

if exist "%mountDir%\pom.xml" (
    set "LANGUAGE=java"
) else (
    if exist "%mountDir%\build.gradle" (
        set "LANGUAGE=java"
    ) else (
        if exist "%mountDir%\tsconfig.json" (
            set "LANGUAGE=ts"
        ) else (
            if exist "%mountDir%\pyproject.toml" (
                set "LANGUAGE=python"
            ) else (
                if exist "%mountDir%\setup.py" (
                    set "LANGUAGE=python"
                )
            )
        )
    )
)
)

::echo POINT 4
echo LANGUAGE = !LANGUAGE!

if not defined LANGUAGE (
    echo.
    echo [ERROR] Unable to detect project language. Please specify -j, -t, or -p
    echo.
    call :print_help
    exit /b 1
)


if defined projectKey (
set "SONAR_PROJECT_KEY=%projectKey%"
set "PROJECT_NAME=%projectKey%"
)
echo SONAR_PROJECT_KEY = !SONAR_PROJECT_KEY!
echo PROJECT_NAME = !PROJECT_NAME!

::echo POINT 5
:: === Set default paths per language
if /I "%LANGUAGE%"=="java" (
    set "ABS_JAVA_SRC=%mountDir%\src"
	set "JAVA_SRC=src\main"
	set "JAVA_SRC_TEST=src\test"
    set "ABS_JAVA_COVERAGE=%mountDir%\%coveragePath%jacoco.xml"
	if defined coveragePath (
        set "JAVA_COVERAGE=%coveragePath%jacoco.xml"
    )
	set "JAVA_CONFIG=%configPath%"
	set "EXCLUSION_REGEX=%exclusions%"
	set "INCLUSION_REGEX=%inclusions%"
	set "PROJECT_TAGS=%tags%"
)

if not defined JAVA_COVERAGE (
	set "JAVA_COVERAGE=target\site\jacoco\jacoco.xml"
	)

if not defined JAVA_CONFIG (
	set "JAVA_CONFIG=target/classes"
	)

::echo POINT 6
if /I "%LANGUAGE%"=="ts" (
    if exist "%mountDir%\src" (
	set "TS_SRC=src"
        set "ABS_TS_SRC=%mountDir%\src"
    ) else if exist "%mountDir%\source" (
		set "TS_SRC=source"
        set "ABS_TS_SRC=%mountDir%\source"
    )
    set "ABS_TS_COVERAGE=%mountDir%\%coveragePath%lcov.info"
	if defined coveragePath (
        set "TS_COVERAGE=%coveragePath%lcov.info"
    )
	set "TS_CONFIG=%configPath%"
	set "EXCLUSION_REGEX=%exclusions%"
	set "INCLUSION_REGEX=%inclusions%"
	set "PROJECT_TAGS=%tags%"
)

if not defined TS_COVERAGE (
	set "TS_COVERAGE=lcov.info"
	)
if not defined TS_CONFIG (
	set "TS_CONFIG=%mountDir%\tsconfig.json"
	)

::echo POINT 7
if /I "%LANGUAGE%"=="python" (
    set "ABS_PY_SRC=%mountDir%\src"
	set "PY_SRC=src"
    set "ABS_PY_COVERAGE=%mountDir%\coverage.xml"
	if defined coveragePath (
        set "PY_COVERAGE=%coveragePath%coverage.xml"
    )
	set "EXCLUSION_REGEX=%exclusions%"
	set "INCLUSION_REGEX=%inclusions%"
	set "PROJECT_TAGS=%tags%"
	
)

if not defined PY_COVERAGE (
	set "PY_COVERAGE=coverage.xml"
	)
::echo POINT 8
:: === Output Debug Info
echo.
echo [INFO] mountDir: %mountDir%
echo [INFO] Detected LANGUAGE: %LANGUAGE%

::echo POINT 9
if /I "%LANGUAGE%"=="java" (
    echo [DEBUG] JAVA Source: %ABS_JAVA_SRC% 
    echo [DEBUG] JAVA Coverage abs path: %ABS_JAVA_COVERAGE%
	echo [DEBUG] JAVA Coverage path: %JAVA_COVERAGE%
	echo [DEBUG] JAVA Sonar Exclusion Folders: %EXCLUSION_REGEX%
	echo [DEBUG] JAVA Sonar Inclusion Folders: %INCLUSION_REGEX%
)

::echo POINT 10
if /I "%LANGUAGE%"=="ts" (
    echo [DEBUG] TS Source: %ABS_TS_SRC%
    echo [DEBUG] TS Coverage abs path: %ABS_TS_COVERAGE%
	echo [DEBUG] TS Coverage path: %TS_COVERAGE%
	echo [DEBUG] TS Sonar Exclusion Folders: %EXCLUSION_REGEX%
	echo [DEBUG] TS Sonar Inclusion Folders: %INCLUSION_REGEX%
    )

::echo POINT 11
if /I "%LANGUAGE%"=="python" (
    echo [DEBUG] PY Source: %ABS_PY_SRC%
    echo [DEBUG] PY Coverage abs path: %ABS_PY_COVERAGE%
	echo [DEBUG] PY Coverage: %PY_COVERAGE%
	echo [DEBUG] PY Sonar Exclusion Folders: %EXCLUSION_REGEX%
	echo [DEBUG] PY Sonar Inclusion Folders: %INCLUSION_REGEX%
   )

echo.
echo [INFO] Script ready.

::echo POINT 12
:: ======================== Sonar Metadata ============================
:: === Extract Project Name and Key if not specified explicitly ===
if not defined SONAR_PROJECT_KEY (
for %%F in ("%mountDir%") do set "PROJECT_NAME=%%~nxF"
	set "SONAR_PROJECT_KEY=%PROJECT_NAME%"
)


echo.
echo [DEBUG] ANALYSIS STEP USING :
echo [DEBUG] Extracted project name: %PROJECT_NAME%
echo [DEBUG] Using project key: %SONAR_PROJECT_KEY%
echo [DEBUG] Using tags : %PROJECT_TAGS%
echo.

set SONAR_PROJECT_VERSION=1.0
set SONAR_TOKEN=squ_0f1ca1623532530eca0c29ec1ef07518eb10a413
set SONAR_HOST=http://10.0.1.50:9000
::set SONAR_TOKEN=b0bf4097ae78b8d898eba8108a4730132e335c07
::set SONAR_HOST=http://localhost:9000

echo.
echo [DEBUG] Preparing to run SonarQube analysis...
echo [DEBUG] LANGUAGE = !LANGUAGE!
echo.

echo [DEBUG] [DEBUG] EXCLUSION = %EXCLUSION_REGEX%

:: === TypeScript Scanner Block ===
if /i "!LANGUAGE!"=="ts" (
 pushd "%mountDir%"
   
    npm config set strict-ssl false

    echo [DEBUG] Installing SonarQube Scanner for TypeScript...
    npm install --save-dev sonarqube-scanner
    popd
	
	pushd "%mountDir%"
     echo [DEBUG] Running SonarQube Scanner with npx...
    npx sonarqube-scanner -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
        -Dsonar.projectName=%PROJECT_NAME% ^
        -Dsonar.projectVersion=%SONAR_PROJECT_VERSION% ^
		-Dsonar.host.url=%SONAR_HOST% ^
        -Dsonar.login=%SONAR_TOKEN% ^
        -Dsonar.javascript.lcov.reportPaths=%TS_COVERAGE% ^
		-Dsonar.exclusions=%EXCLUSION_REGEX% ^
		-Dsonar.inclusions=%INCLUSION_REGEX%^
		-Dsonar.project.tags=%PROJECT_TAGS%
	popd
)

:: === Java Scanner Block ===
if /i "!LANGUAGE!"=="java" (
    pushd "%mountDir%"
    echo [DEBUG] Running Maven build...
    mvn clean install -T 8C -Drun.jvmArguments="-Xmx8192m -xms8192"
	popd
	pushd "%mountDir%"
    echo [DEBUG] Running SonarQube Scanner for Java via Maven...
    mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -X ^
        -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
        -Dsonar.projectName=%PROJECT_NAME% ^
        -Dsonar.projectVersion=%SONAR_PROJECT_VERSION% ^
        -Dsonar.host.url=%SONAR_HOST% ^
        -Dsonar.login=%SONAR_TOKEN% ^
        -Dsonar.sources=%JAVA_SRC% ^
        -Dsonar.java.binaries=%JAVA_CONFIG% ^
        -Dsonar.coverage.jacoco.xmlReportPaths=%JAVA_COVERAGE% ^
		-Dsonar.exclusions=%EXCLUSION_REGEX% ^
		-Dsonar.inclusions=%INCLUSION_REGEX% ^
		-Dsonar.project.tags=%PROJECT_TAGS%
    popd
)

:: === Python Scanner Block ===
if /i "!LANGUAGE!"=="python" (
    pushd "%mountDir%"
    echo [DEBUG] inside python block
    set "SCANNER_ZIP=sonar-scanner.zip"
    set "SCANNER_DIR=sonar-scanner"
    set "SCANNER_URL=https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.8.0.2856-windows.zip"
    set "SCANNER_EXTRACT_DIR=%mountDir%\!SCANNER_DIR!"	
    set "SCANNER_BIN=!SCANNER_EXTRACT_DIR!\sonar-scanner-4.8.0.2856-windows\bin\sonar-scanner.bat"
    
    if not exist "!SCANNER_BIN!" (
        echo [DEBUG] Downloading SonarScanner CLI...
        powershell -Command "Invoke-WebRequest -Uri '!SCANNER_URL!' -OutFile '!SCANNER_ZIP!'"
        
        echo [DEBUG] Extracting SonarScanner...
        powershell -Command "Expand-Archive -Path '!SCANNER_ZIP!' -DestinationPath '!SCANNER_EXTRACT_DIR!'"

        del "!SCANNER_ZIP!"
    ) else (
        echo [DEBUG] SonarScanner already installed locally.
    )
	popd
	pushd "%mountDir%"
    echo [DEBUG] Running SonarScanner CLI for Python...
    call "!SCANNER_BIN!" ^
        -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
        -Dsonar.projectName=%PROJECT_NAME% ^
        -Dsonar.projectVersion=%SONAR_PROJECT_VERSION% ^
        -Dsonar.host.url=%SONAR_HOST% ^
        -Dsonar.login=%SONAR_TOKEN% ^
        -Dsonar.sources=%PY_SRC% ^
		-Dsonar.python.coverage.reportPaths=%PY_COVERAGE% ^
		-Dsonar.exclusions=%EXCLUSION_REGEX% ^
		-Dsonar.inclusions=%INCLUSION_REGEX% ^
		-Dsonar.project.tags=%PROJECT_TAGS%
    
    popd
)

echo.
echo === SonarQube analysis complete ===
endlocal
exit /b