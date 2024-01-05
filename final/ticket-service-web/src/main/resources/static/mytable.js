// based on https://github.com/KasperOlesen/DataTable-AltEditor/blob/master/example/12_inline_buttons/example12.js
//TODO the library contains modifications, diff it and vendor or inject.
$(document).ready(function () {
    let url = ""; //TODO
    // geez https://stackoverflow.com/questions/12199051/merge-two-arrays-of-keys-and-values-to-an-object-using-underscore
    zipToObj = (names, vals) => names.reduce((acc, k, i) => (acc[k] = vals[i], acc), {});
    tableNode = function(datatable){ return datatable.s.dt.table().node(); }
    entityName = function(datatable){ return tableNode(datatable).dataset.entity; }
    csrfHandler = function(xhr){
        xhr.setRequestHeader(csrfHeader, csrfToken);
    }
    getOptSet = function(setName) {
        return myEnums[setName];
    }

    onAddRow = function (datatable, rowdata, success, error) {
        var mycolnames = datatable.s.dt.settings()[0].aoColumns.map(e => e.colname);
        var myobj = zipToObj(mycolnames, rowdata);
        if(entityName(datatable) === "seat"){ // nested table //TODO generic functionality
            var searchparent = $(tableNode(datatable)).parent(); //closest also matches the current node
            var parenttablenode = searchparent.closest('table')
            var parentidx = searchparent.closest('tr').index()
            var parentdt = parenttablenode.DataTable()
            var colnames = parentdt.settings()[0].aoColumns.map(e => e.colname);
            var parentrowdata = parentdt.row(parentidx).data();
            var parentobj = zipToObj(colnames, parentrowdata);

            //TODO probably some kind of mapping library for json
            myobj = {
                ticketid: parentobj.ticketid,
                screening: { roomname: parentobj.roomname, movietitle: parentobj.movietitle, time: parentobj.time},
                seat: { row: myobj.row, col: myobj.col }
            }
        }
        $.ajax({
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({...{entity: entityName(datatable)}, ...myobj}),
            success: success,
            error: error,
            beforeSend: csrfHandler
        });
    }

    onEditRow = function (datatable, rowdata, success, error) {
        var mycolnames = datatable.s.dt.settings()[0].aoColumns.map(e => e.colname);
        var myobj = zipToObj(mycolnames, rowdata);

        $.ajax({
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({...{entity: entityName(datatable)}, ...myobj}),
            success: success,
            error: error,
            beforeSend: csrfHandler
        });
    }

    onDeleteRow = function (datatable, rowdata, success, error) {
        //TODO this time rowdata is an array, wonderful.
        //TODO hack down to a single row
        var mycolnames = datatable.s.dt.settings()[0].aoColumns.map(e => e.colname);
        var myobj = zipToObj(mycolnames, rowdata[0]);

        $.ajax({
            type: 'DELETE',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({...{entity: entityName(datatable)}, ...myobj}),
            success: success,
            error: error,
            beforeSend: csrfHandler
        });
    }
    
    // Init each table
    $('table').each(function(i, table) {
        // Add column for buttons
        //TODO do it semantically?
        $(table).find(">thead>tr").append("<th></th>");
        $(table).find(">tbody>tr").append("<td></td>");
        var colSpecs = Array.from($(table).find(">thead>th").map(function(i,e){
            var base = { targets: i, data: e.dataset.colname }; //TODO will this break? we are setting the data source specifier butwe are using the static html table as the data source
            var select = (e.dataset.selecttype !== undefined) ? { // TODO the css for these is also worse than the demo for some reason? might be because im usin a 2021 release and bootstrap broke?
                type: "select",
                select2: { width: "100%" },
                options: getOptSet(e.dataset.selecttype)
                /*render: function (data, type, row, meta) { //TODO why is this using a custom render function?
                    var optSet = getOptSet(e.dataset.selecttype)
                    if (data == null || !(data in optSet)) return null;
                    return optSet[data];
                }*/
            } : {}
            return {...base, ...select};
        })).filter(function(e){ return e.data !== undefined}); //TODO figure out what to do here now that multiple fields in the config

        let colCount = $(table).find('>thead>tr:nth-child(1)>th').length; //TODO can simplify?
        let mtable = $(table).DataTable({ //This way we can get it from the element again?
            order: [],
            dom: 'Bfrtip',        // Needs button container
            select: {
                style: 'single',
                toggleable: false
            },
            altEditor: true,     // Enable altEditor
            buttons: [],          // no buttons, however this seems compulsory
            responsive: true,
            columnDefs: colSpecs.concat([{
                //targets: colCount-1, //TODO need to do this per table
                targets: -1, //TODO need to do this per table
                data: null,
                title: "Actions",
                name: "Actions",
                render: function (data, type, row, meta) {
                    return '<a class="delbutton fa fa-minus-square btn btn-danger" href="#"></a>';
                },
                disabled: true
            }]),
            onAddRow: onAddRow,
            onEditRow: onEditRow,
            onDeleteRow: onDeleteRow,
        });

        // Add "add row" row
        //TODO how does this interact with datatable?
        const buttonFooter = `
        <tfoot>
        <tr>
            <td colspan="${colCount}">
                <button class="btn btn-primary addbutton" title="Add"><span class="fa fa-plus-square"></span></button>
            </td>
        </tr>
        </tfoot>
        `;
        if (!("noadd" in table.dataset)) {
            //Something something tfoot is specced to be after thead? TODO
            $(table).find('>thead').after(buttonFooter);
        }
    });

    // Edit
    $(document).on('click', "table tbody ", 'tr', function (event) {
        var tableID = $(this).closest('table').attr('id');    // id of the table
        var that = $('#' + tableID)[0].altEditor;
        event.stopPropagation(); // TODO theres probably a whole lot of interactivity thatdoesnt account for nested tables?
        that._openEditModal();
        $('#altEditor-edit-form-' + that.random_id)
            .off('submit')
            .on('submit', function (e) {
                e.preventDefault();
                e.stopPropagation();
                that._editRowData();
            });
    });

    // Delete
    $(document).on('click', "table .delbutton", 'tr', function (x) {
        var tableID = $(this).closest('table').attr('id');    // id of the table
        var that = $('#' + tableID)[0].altEditor;
        that._openDeleteModal();
        $('#altEditor-delete-form-' + that.random_id)
            .off('submit')
            .on('submit', function (e) {
                e.preventDefault();
                e.stopPropagation();
                that._deleteRow();
            });
        x.stopPropagation(); //avoid open "Edit" dialog
    });

    // Add
    $('.addbutton').on('click', function (event) {
        event.stopPropagation(); // Dont open the row editor when clicking the button in nested tables
        var tableID = $(this).closest('table').attr('id');
        var that = $('#' + tableID)[0].altEditor;
        that._openAddModal();
        $('#altEditor-add-form-' + that.random_id)
            .off('submit')
            .on('submit', function (e) {
                e.preventDefault();
                e.stopPropagation();
                that._addRowData();
            });
    });
});