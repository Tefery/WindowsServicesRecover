# WindowsServicesRecover

WindowsServicesRecover nace a raíz de que algo se ha follado mis servicios esenciales de Windows, así que he creado esta herramienta para importar los servicios básicos de Windows 10 y que por lo menos funcione algo.

## Cómo funciona

Hay que partir de un archivo ***.reg*** (de esos para modificar el registro de windows) exportado de una instalación limpia de Windows de la misma versión que tu instalación actual. 
> Limpia de recién instalado, no de tu primo el que parece que le va bien


La clave a exportar es exactamente: `HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services`

![](https://raw.githubusercontent.com/Tefery/WindowsServicesRecover/main/exportPhoto.png)

Hay un fichero de registro de un Windows 10 20H2 en la raiz del proyecto [servides.reg](https://github.com/Tefery/WindowsServicesRecover/blob/main/servides.reg). Es la que he extraido y usado yo, y parece que el pc ha vuelto a un estado mas o menos estable.

Se compila con la [JDK15](https://jdk.java.net/15/) y se le envia como argumento el archivo ***.reg***

Si algún dia subo un .jar, se ejecutaría con:

`Java -jar WindowsServicesRecover.jar servides.reg`

Recomiendo ejecutarlo estándo en modo a prueba de errores con simbolo del sistema.

### Desconozco que posibles problemas puede causar este proceso a Windows a largo plazo, no me responsabilizo de cualquier tipo de daño o error provocado por esta aplicación a tu equipo, si lo usaste mal o sin necesidad, apoquina con las consecuencias.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[GNU AGPLv3](https://choosealicense.com/licenses/agpl-3.0/)
