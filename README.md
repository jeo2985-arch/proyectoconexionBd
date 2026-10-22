# CRUD Alumnos — Azure SQL + GitHub Codespaces

Aplicación Java 21 / Maven para administrar `dbo.Alumnos` en la base `AvanzadaII` de Azure SQL Database.

## Requisitos
- Azure SQL Database creada, accesible por red y con tabla `dbo.Alumnos`.
- Usuario de SQL con permisos SELECT/INSERT/UPDATE/DELETE sobre `dbo.Alumnos`.
- Acceso de red del Codespace autorizado en firewall / networking de Azure SQL.

## Instalación en tu repositorio
Copia `pom.xml`, `.gitignore`, `.devcontainer/`, `src/` y este README al repositorio. Los archivos en `src/` reemplazan el CRUD anterior: haz respaldo antes de reemplazar.

## Codespaces Secrets
En GitHub entra a **Settings → Codespaces → Secrets → New secret** y crea:

- `DB_HOST` = nombre del servidor de Azure SQL, SIN `jdbc:sqlserver://` ni `:1433`
- `DB_NAME` = `AvanzadaII`
- `DB_USER` = usuario SQL habilitado para la base
- `DB_PASSWORD` = contraseña SQL

Concede acceso al repositorio `proyectoconexionBd`, y reinicia el Codespace si ya estaba abierto. Evita guardar secretos en `.env`, `pom.xml`, en archivos Java o en el historial Git.

## Ejecución
Abre GitHub → repositorio → Code → Codespaces → Create codespace on main. Si modificaste `.devcontainer`, usa la opción **Rebuild Container**.

En terminal:

```bash
java -version
mvn -version
mvn clean compile
mvn exec:java
```

También puedes compilar sin conexión a SQL; la conexión se prueba cuando se ejecuta `mvn exec:java`.

## Publicar cambios
```bash
git add .
git commit -m "Mejora CRUD alumnos compatible con Codespaces y Azure SQL"
git push origin main
```

Si `target/` ya estaba versionado, retíralo del índice con `git rm -r --cached target` antes de hacer commit.

## Seguridad
No se deshabilita TLS ni se usa `trustServerCertificate=true`. No publiques un servidor SQL con acceso irrestricto como solución permanente. Usa credenciales de mínimo privilegio y rota cualquier contraseña que hayas expuesto previamente en código, chats o repositorios.

## Diagnóstico de errores
- Login failed: usuario/contraseña/permiso incorrectos.
- Cannot open server / Client with IP not allowed: revisar firewall Azure SQL para IP pública de salida de Codespaces.
- SSL / certificate: comprobar nombre DNS del servidor, CA, hora y configuración TLS.
- Invalid object name `dbo.Alumnos`: comprobar base/tabla/esquema.
