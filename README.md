# datatables2tabledit

### Dt2Te: Datatables & Tabledit integration
- Datatables: https://datatables.net/
- JQuery-Tabledit: https://markcell.github.io/jquery-tabledit
- **Integration jquery plugin:** [dataTables.2.tableedit.js](src/main/resources/public/content/js/dataTables.2.tableedit.js)
Currently it only supports `<input>` and `<select>` form elements.
- **Integration plugin usage:** [script.js](src/main/resources/public/content/js/script.js)
- In this example, data of the right side table depends on data of the left side table. <br>
The Left side table defines 'field's & every column on the right side table is a 'field', except id(#) column.
- **Backend:** Spring Boot, Spring Rest; **Frontend:** Thymeleaf
- **Installation:**
  - Clone the project
  - run `mvn install` in the project directory
  - Run PocApplication class
  - Open http://localhost:8080 in your browser
  - Click around & find your way

![UI example:](doc/dt2te.png "UI screeshot")

