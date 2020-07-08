# **Por favor leer antes de usar**
Para el correcto funcionamiento en el Nanopi NEO Plus2, se debe instalar la librería de **pi4j**, la cual es necesaria para interactuar con los GPIOs del hardware, ya sea para el uso del I2C, Buzzer, Wiegand o RS422. Para esto es necesario compilar la librería, dado que la versión que se encuentra en la página oficial está compilada para una arquitectura distinta `arm32` que la del procesador del Nanopi usado aquí `arm64`.

Para esto es necesario seguir los siguientes pasos, luego de acceder al Nanopi a través de `ssh`:

# Instalar paquetes

Los paquetes a instalar son:
- `openjdk8`: Es necesario tener la versión 8 del SDK abierto de Java, dado que la librería funciona sólo con esta versión
- `git`: Para descargar el repositorio que contiene el código de fuente usamos git
- `tree`: El proceso de compilación usa este comando para revisar los directorios dentro del repositorio descargado
- `maven`: Una herramienta para manejar dependencias y diferentes aspectos como la compilación de un projecto de Java

Para los primeros tres, usamos el siguiente comando:

```
sudo apt install openjdk-8-jdk git-core tree
```

Para instalar `maven`, este caso la versión `3.6.3`, se deben correr los siguientes comandos:

```
wget https://www-us.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -P /tmp
sudo tar xf /tmp/apache-maven-*.tar.gz -C /opt
sudo ln -s /opt/apache-maven-3.6.3 /opt/maven
```

Luego de deben agregar las siguientes variables de entorno al archivo `/etc/profile.d/maven.sh` (si no existe se crea):

```
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-arm64
export M2_HOME=/opt/maven
export MAVEN_HOME=/opt/maven
export PATH=${M2_HOME}/bin:${PATH}
```

Con esto podemos compilar la librería usando la siguiente lista de comandos:

```
cd ~
mkdir tmp
cd tmp
git clone https://github.com/Pi4J/pi4j
cd pi4j
mvn clean install -P all-platforms,local-compile
cd pi4j-distribution/target
sudo apt install ./pi4j-1.2-SNAPSHOT.deb
```

Luego de esto, la librería debería estar disponible para usar. Para confirmar se debe usar el comando:

```
pi4j -v
```