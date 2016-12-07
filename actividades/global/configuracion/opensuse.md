
# Configurar MV GNU/Linux OpenSUSE 13.2

> * Donde aparezca AA debemos poner el código asignado al aula:
>     * 18 para el aula108
>     * 19 para el aual109
> * Donde aparezca XX debemos poner el código asignado al alumno.

## Durante la instalación

Recomendaciones:
* Usar un disco VirtualBox de 15GB.
* Seleccionar entorno gráfico ligero como Xfce.
* Configurar lo siguiente durante la instalación:

![opensuse-instalacion-configuracion.png](../images/opensuse-instalacion-configuracion.png)

## Proceso de configuración con Yast

Una vez instalado el sistema operativo, podemos hacer cambios en la configuración,
usando la herramienta `Inicio -> Configuración -> Yast`. Luego iremos a la
opcion de `Ajustes de red`.

Vamos a `Vista resumen -> Interfaz -> Editar`
* Marcamos IP fija.
* IP: `172.AA.XX.31` (Donde XX corresponde al nº de cada puesto).
    * Si tenemos varias máquinas usaremos las IP 172.AA.XX.32, 172.AA.XX.33, etc.
    * Máscara de red: `255.255.0.0`
* Siguiente

Vamos a `Nombre de Host/DNS` y ponemos:
* Desmarcamos `Modificar nombre mediante DHCP`
* Marcamos `Asignar nombre de host a la IP bucle local`
* Nombre de equipo: `primer-apellido-del-alumnoXXg`.
    * Por ejemplo vargas30g
    * Si tenemos varias máquinas las llamaremos vargas30h, vargas30i, vargas30j, etc.
* Nombre de dominio: `curso1617` (Modificar los números al curso actual).
* Servidor DNS: `8.8.4.4`
Vamos a `Encaminamiento`y ponemos:
* Gateway o pasarela IPv4: `172.AA.0.1`. Esto es la puerta de enlace o encaminamiento.
* Ir dispositivo y elegir interfaz de red.
* Usuarios:
    * Un usuario identificado con `nombre-del-alumno`.
    * Poner al usuario `root` la clave del alumno con la letra en minúscula.

> **ATENCIÓN**
>
> * Los nombres de usuario, máquina y dominio deben estar en minúsculas.
Sin usar caracteres especiales como ñ, tildes, espacios, etc.
> * Asegurarse de que el nombre de host está correctamente en el fichero `/etc/hosts`.
Para que el comando hostname funcione bien.

* Tarjeta de red VBox en `modo puente`.
* Configurar [acceso remoto](../acceso-remoto.md).
* Configurar [firewall](../firewall.md).

## Comprobaciones finales

Capturar imágen de la configuración del equipo:
```
date
uname -a
hostname -f #Muestra nombre-maquina.nombre-dominio
hostname -a #Muestra nombre-maquina
hostname -d #Muestra nombre-dominio

tail -n 5 /etc/passwd
ip a
route -n
ping 8.8.4.4
host www.iespuertodelacruz.es
blkid
```

## Entorno gráfico

Para configurar la red por entorno gráfico vamos a
`Inicio -> Yast -> Ajustes de red`

## Ficheros de configuración

El fichero de configuración de red de OpenSUSE es `/etc/sysconfig/network/ifcfg-eth0`
En el modo automático tiene un contenido similar al siguiente:
```
BOOTPROTO='dhcp'
STARTMODE='auto'
DHCLIENT_SET_DEFAULT_ROUTE='yes'
```
En el modo manual puede ser como:
```
BOOTPROTO='static'
IPADDR='192.168.16.11/24'
STARTMODE='ifplugd'
```