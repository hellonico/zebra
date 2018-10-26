export VERSION=6.7.4981


# mvn install:install-file -Dfile=lib/com.google.ortools.jar -DgroupId=com.google \
# -DartifactId=ortools -Dversion=$VERSION -Dpackaging=jar

# mvn install:install-file -Dfile=lib/com.google.ortools.jar -DgroupId=com.google \
# -DartifactId=ortools -Dversion=$VERSION -Dpackaging=jar


mvn deploy:deploy-file \
-Dfile=com.google.ortools.jar \
-DgroupId=com.google \
-DartifactId=ortools \
-Dversion=$VERSION \
-Dpackaging=jar \
-DrepositoryId=vendredi \
-Durl=http://hellonico.info:8081/repository/hellonico/

