
/************************************************************

    Dt2Te: Datatables & Tabledit integration

    Datatables: https://datatables.net/
    JQuery-Tabledit: https://markcell.github.io/jquery-tabledit

*************************************************************/

if (typeof jQuery === 'undefined') {
    throw new Error('Dt2Te requires jQuery library!');
}

(function ($) {
    'use strict';

    $.fn.Dt2Te = function (options) {

        // VARIABLES
        var $dataTable = null, $tableEdit = null, $this = this, initialDraw = true;

        // OPTIONS
        var defaults = {
            tableId: null,
            dataTables: {
                ajaxUrl: '',
                validSelectsProperty: null,
                addNewRowText: 'Add Row',
                addTableRowClass: null,
                dataProperty: null,
                // plain row html without tabledit
                tableRowHtml: null,
                iDisplayLength: 8,
                pageLength: 8,
                searchDelay: 750,
                autoFocus: true,
                autoWidth: false,
                bProcessing: true,
                bInfo: false,
                bSortable: true,
                order: [],
                bAutoWidth: false,
                searching: true,
                lengthChange: false,
                scrollCollapse: false,
                columns: [],
                language: {
                    "lengthMenu": "Show _MENU_ rows in a page",
                    "zeroRecords": "No records found!",
                    "info": "You are presented _PAGE_ th page, total pages: _PAGES_",
                    "infoEmpty": "No records",
                    "infoFiltered": "(Fitered from _MAX_ records)",
                    "sSearch": "Search",
                    "oPaginate": {
                        "sFirst": "First Page",
                        "sPrevious": "Previous",
                        "sNext": "Next",
                        "sLast": "Last Page"
                    }
                }
            },
            tableEdit: {
                url: null,
                identifier: null,
                addRowContent: null
            },
            // {'1': ['apple', 'orange']}
            validSelectTypes: {}
        };

        var settings = $.extend(true, {}, defaults, options);

        // API
        $this.api = {
            showSettings: function () {
                console.dir(settings);
            },
            dataTables: {
                renderTable: function () {
                    $dataTable = $this.DataTable({
                        ajax: {
                            url: settings.dataTables.ajaxUrl,
                            dataSrc: function (json) {
                                settings.validSelectTypes = json[settings.dataTables.validSelectsProperty];
                                return json[settings.dataTables.dataProperty]
                            }
                        },
                        dom: 'Bfrtip',
                        buttons: [
                            {
                                text: '<i class="fas fa-plus"></i>  ' + settings.dataTables.addNewRowText,
                                action: function (e, dt, node, config) {
                                    $this.api.tableEdit.handleAddTableRow();
                                },
                                className: 'btn btn-sm btn-info jqt-small-button ' + settings.dataTables.addTableRowClass
                            }
                        ],
                        iDisplayLength: settings.dataTables.iDisplayLength,
                        pageLength: settings.dataTables.pageLength,
                        searchDelay: settings.dataTables.searchDelay,
                        autoFocus: settings.dataTables.autoFocus,
                        autoWidth: settings.dataTables.autoWidth,
                        bProcessing: settings.dataTables.bProcessing,
                        bInfo: settings.dataTables.bInfo,
                        bSortable: settings.dataTables.bSortable,
                        order: settings.dataTables.order,
                        bAutoWidth: settings.dataTables.bAutoWidth,
                        searching: settings.dataTables.searching,
                        lengthChange: settings.dataTables.lengthChange,
                        scrollCollapse: settings.dataTables.scrollCollapse,
                        columns: settings.dataTables.columns,

                        "rowCallback": function (row, data) {
                            var $html = $(settings.dataTables.tableRowHtml);
                            var isTableEdit = false;
                            for (var i = 0; i < settings.dataTables.columns.length; i++) {
                                var $span = $('td:eq(' + i + ') span.tabledit-span', row);
                                if ($span.length == 0)
                                    break;
                                else {
                                    isTableEdit = true;
                                    $html.find('td:eq(' + i + ')').text($span.text());
                                }
                            }
                            if (isTableEdit)
                                $(row).html($html.find("#row").html());
                        },
                        "preDrawCallback": function (dtSettings) {
                            // remove default datatables button formatting 
                            $('button.' + settings.dataTables.addTableRowClass).removeClass('dt-button');
                        },
                        language: settings.dataTables.language
                    });

                },
                reload: function () {
                    $dataTable.ajax.reload();
                },
                attachDrawDt: function (callback) {
                    $dataTable.on('draw.dt', function () {
                        // jquery table edit 
                        $this.api.tableEdit.renderTableEdit();

                        //adjust headers to take full width
                        $($.fn.dataTable.tables(true)).DataTable().columns.adjust();

                        // reload dependent Dt2Te tables' data
                        if (callback && !initialDraw)
                            callback();

                        initialDraw = false;
                    });
                }
            },
            tableEdit: {
                renderTableEdit: function () {
                    var editableColumns = [], cloneSelectTypes = JSON.parse(JSON.stringify(settings.validSelectTypes));

                    for (var i = 1; i < settings.dataTables.columns.length; i++) {
                        var col = settings.dataTables.columns[i];
                        if (col.type == 'input')
                            editableColumns.push([i, col.valueField]);
                        else if (col.type == 'select')
                            // ui column order & settings.validSelectTypes order must be the same
                            editableColumns.push([i, col.valueField, JSON.stringify(cloneSelectTypes.shift())]);
                    }

                    $tableEdit = $this.Tabledit({
                        url: settings.tableEdit.url,
                        dataType: 'json',
                        hideIdentifier: false,
                        editButton: true,
                        columns: {
                            identifier: [0, settings.tableEdit.identifier],
                            editable: editableColumns
                        },
                        buttons: {
                            edit: {
                                class: 'btn btn-xs btn-outline-secondary edit',
                                html: '<span class="fas fa-pen-square"></span>',
                                action: 'edit'
                            },
                            save: {
                                class: 'btn btn-xs btn-outline-secondary jqt-small-button',
                                html: 'Kaydet'
                            },
                            confirm: {
                                class: 'btn btn-xs btn-danger jqt-small-button',
                                html: 'Onayla'
                            },
                            delete: {
                                class: 'btn btn-xs btn-danger',
                                html: '<span class="fas fa-trash-alt"></span>',
                                action: 'delete'
                            }
                        },
                        restoreButton: false,
                        // onAjax: function (action, serialize) {
                        // 	DILISIM.showLoading();
                        // },
                        onSuccess: function (data, textStatus, jqXHR) {
                            $dataTable.ajax.reload();
                            Common.showNotif('success', 'Güncelleme başarılı!', 'İlgili kayıtlar başarıyla güncellendi.');
                            // Her işlemden sonra bunu false olarak ayarlayalım. Eğer değişmesi gereken başka alanlar 
                            // varsa tabloyu tekrar çizerken bunu true yapacak.
                            // mustChange = false;
                        },
                        onDraw: function () {
                        },
                        onFail: function (jqXHR, textStatus, errorThrown) {
                            console.dir(errorThrown);
                            $dataTable.ajax.reload();
                            Common.showNotif('warning', 'Güncellerken bir hata oluştu!', 'İlgili kayıtlar güncellenemedi.');
                        }
                    });
                },
                handleAddTableRow: function () {
                    var body = $(settings.tableId + ' tbody'),
                        $rowContent = $(settings.tableEdit.addRowContent),
                        $select = $rowContent.find('select');

                    $.each($select, function (index, s) {
                        var $currentSelect = $(s);
                        for (const item in settings.validSelectTypes[index]) {
                            $currentSelect.append($('<option>', {
                                value: item,
                                text: settings.validSelectTypes[index][item]
                            }));
                        }
                    });

                    body.append($rowContent);
                    $('table' + settings.tableId + ' button.tabledit-edit-button').last().trigger('click');
                }
            }
        }

        return this;
    };

}(jQuery));