# WindowsServicesRecover

WindowsServicesRecover nace a raíz de que algo se ha follado mis servicios esenciales de Windows, así que he creado esta herramienta para importar los servicios básicos de Windows 10 y que por lo menos funcione algo.

## Cómo funciona

Hay que partir de un archivo ***.reg*** (de esos para modificar el registro de windows) exportado de una instalación limpia de Windows de la misma versión que tu instalación actual. 
> Limpia de recién instalado, no de tu primo el que parece que le va bien


La clave a exportar es exactamente: `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services`

![](https://raw.githubusercontent.com/Tefery/WindowsServicesRecover/main/exportPhoto.png)

Hay un fichero de registro de un Windows 10 Pro 20H2 en la raiz del proyecto [servides.reg](https://github.com/Tefery/WindowsServicesRecover/blob/main/servides.reg). Es la que he extraido y usado yo, y parece que el pc ha vuelto a un estado mas o menos estable.

Y os estaréis preguntando "¿Por qué voy a usar tu mierda de aplicación, si puedo hacer doble click en el archivo y ya está?” pues habría estado cojonudo que funcionase así, pero no lo hace, porque hay ciertos componentes de Windows que están usando estos registros, y con uno que falle, se cancela todo, así que hay que insertarlo de poquitos en poquitos.

Se compila con la [JDK14](https://jdk.java.net/archive/) o superior y se le envia como argumento el archivo ***.reg***. Yo recomiendo usar la [JDK15](https://jdk.java.net/15/).

El ***.jar*** está compilado para la JDK8, ya que es la version que java que tienen practicamente el total de los mortales.

El comando para ejecutarlo con el ***.jar***: `Java -jar WindowsServicesRecover.jar servides.reg`

Recomiendo ejecutarlo estándo en modo a prueba de errores con simbolo del sistema.

Incluye un modo de solo división con el argumento `/d`, para solo dividir el archivo de registro, por si prefieres introducir por tu cuenta los 2.791 archivos de registro o lo sea que quieras hacer con ellos.

### Desconozco que posibles problemas puede causar este proceso a Windows a largo plazo, no me responsabilizo de cualquier tipo de daño o error provocado por esta aplicación a tu equipo, si lo usaste mal o sin necesidad, apoquina con las consecuencias.

## EXTRA EXTRA!
Despues de restaurar todos los servicios, a parte de que a Windows por fin le funcionaba todo, noté que iba así un poco como a pedales. Así que buscando por los mares de github me encontré con [**windows-tools**](https://github.com/jebofponderworthy/windows-tools), y oye, que ahora va como un tiro, como si hubiese reinstalado Windows vamos.
> 100% recomendado real no fake 1 link mega

## Contributing
Cualquier cambio a mejor es bienvenido, soy consciente de que la aplicación es fea y cutre, pero hace lo que se le pide.

## License
[GNU AGPLv3](https://choosealicense.com/licenses/agpl-3.0/)
