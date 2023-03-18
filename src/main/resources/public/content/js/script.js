/************************************************************

    Step3 lib

*************************************************************/

// Global Step3 object
var Step3 = Step3 || {}, dt2TeFields, dt2TeIpRels;

!(function ($) {
    'use strict';

    Step3 = {
        showDocFieldsTable: function () {
            dt2TeFields = $('#fieldsTable').Dt2Te({
                tableId: '#fieldsTable',
                dataTables: {
                    ajaxUrl: "/step3/docuscanDocFieldResult",
                    dataProperty: "fields",
                    validSelectsProperty: 'validFieldSelects',
                    addTableRowClass: 'addFieldTableRow',
                    tableRowHtml: '<div id="wrapper"><table><tr id="row"><td></td><td></td><td></td></tr></table></div>',
                    columns: [
                        {
                            data: "id",
                            title: "#",
                            visible: true
                        },
                        {
                            data: "value",
                            title: "Value",
                            type: "input",
                            valueField: "value"
                        },
                        {
                            data: "fieldTypeText",
                            title: "Field Type",
                            type: "select",
                            valueField: "fieldTypeId"
                        }
                    ]
                },
                tableEdit: {
                    url: '/step3/updateDocField/',
                    identifier: 'fieldId',
                    addRowContent: `<tr id="">
									<td>
									<span class="tabledit-span tabledit-identifier"></span>
									<input class="tabledit-input tabledit-identifier" type="hidden" name="fieldId" value="" disabled="">
									</td>
									<td class="tabledit-view-mode">
									<span class="tabledit-span"></span>
									<input class="tabledit-input form-control input-sm" type="text" name="value" value="" style="display: none;" disabled="">
									</td>
									<td class="tabledit-view-mode">
									<span class="tabledit-span">İlgi</span>
									<select class="tabledit-input form-control input-sm" name="fieldTypeId" style="display: none;" disabled="">
                                    <option disabled selected value>Tip Seçiniz</option>
									</select>
									</td>
									<td style="white-space: nowrap; width: 1%;">
									<div class="tabledit-toolbar btn-toolbar" style="text-align: left;">
										<div class="btn-group btn-group-sm" style="float: none;">
										<button type="button" class="tabledit-edit-button btn btn-xs btn-outline-secondary edit" style="float: none;">
											<span class="fas fa-pen-square"></span>
										</button>
										<button type="button" class="tabledit-delete-button btn btn-xs btn-danger" style="float: none;">
											<span class="fas fa-trash-alt"></span>
										</button>
										</div>
										<button type="button" class="tabledit-save-button btn btn-xs btn-outline-secondary jqt-small-button" style="display: none; float: none;">Kaydet</button>
										<button type="button" class="tabledit-confirm-button btn btn-xs btn-danger jqt-small-button" style="display: none; float: none;">Onayla</button>
									</div>
									</td>
								</tr>`
                }
            });

            // datatables render
            dt2TeFields.api.dataTables.renderTable();

            // on datatables draw.dt event, render edit table
            dt2TeFields.api.dataTables.attachDrawDt(function () {
                dt2TeIpRels.api.dataTables.reload();
            });
        },
        showIpRelationsTable: function () {
            dt2TeIpRels = $('#ipRelationsTable').Dt2Te({
                tableId: '#ipRelationsTable',
                dataTables: {
                    ajaxUrl: "/step3/docuscanIpRelResult",
                    dataProperty: "ipRelations",
                    validSelectsProperty: 'validIpRelationsSeletcs',
                    addTableRowClass: 'addIpRelTableRow',
                    tableRowHtml: `<div id="wrapper">
                                    <table>
                                    <tr id="row">
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    </table>
                                </div>`,
                    columns: [
                        {
                            data: "id",
                            title: "#",
                            visible: true
                        },
                        {
                            data: "ipField.value",
                            title: "IP",
                            type: "select",
                            valueField: "ipField.id"
                        },
                        {
                            data: "portField.value",
                            title: "Port",
                            type: "select",
                            valueField: "portField.id"
                        },
                        {
                            data: "startDateField.value",
                            title: "Start Date",
                            type: "select",
                            valueField: "startDateField.id"
                        },
                        {
                            data: "startTimeField.value",
                            title: "Start Time",
                            type: "select",
                            valueField: "startTimeField.id"
                        }
                    ]
                },
                tableEdit: {
                    url: '/step3/updateIpRelation/',
                    identifier: 'ipRelId',
                    addRowContent: `<tr id="">
                                        <td>
                                        <span class="tabledit-span tabledit-identifier"></span>
                                        <input class="tabledit-input tabledit-identifier" type="hidden" name="ipRelId" value="" disabled="">
                                        </td>
                                        <td class="tabledit-view-mode">
                                        <span class="tabledit-span"></span>
                                        <select class="tabledit-input form-control input-sm" name="ipField.id" style="display: none;" disabled="">
                                        <option disabled selected value>IP Seçiniz</option>
                                        </select>
                                        </td>
                                        <td class="tabledit-view-mode">
                                        <span class="tabledit-span"></span>
                                        <select class="tabledit-input form-control input-sm" name="portField.id" style="display: none;" disabled="">
                                        <option disabled selected value>Port Seçiniz</option>
                                        </select>
                                        </td>
                                        <td class="tabledit-view-mode">
                                        <span class="tabledit-span"></span>
                                        <select class="tabledit-input form-control input-sm" name="startDateField.id" style="display: none;" disabled="">
                                        <option disabled selected value>Başlangıç Tarihi Seçiniz</option>
                                        </select>
                                        </td>
                                        <td class="tabledit-view-mode">
                                        <span class="tabledit-span"></span>
                                        <select class="tabledit-input form-control input-sm" name="startTimeField.id" style="display: none;" disabled="">
                                        <option disabled selected value>Başlangıç Saati Seçiniz</option>
                                        </select>
                                        </td>
                                        <td style="white-space: nowrap; width: 1%;">
                                        <div class="tabledit-toolbar btn-toolbar" style="text-align: left;">
                                            <div class="btn-group btn-group-sm" style="float: none;">
                                            <button type="button" class="tabledit-edit-button btn btn-xs btn-outline-secondary edit" style="float: none;">
                                                <span class="fas fa-pen-square"></span>
                                            </button>
                                            <button type="button" class="tabledit-delete-button btn btn-xs btn-danger" style="float: none;">
                                                <span class="fas fa-trash-alt"></span>
                                            </button>
                                            </div>
                                            <button type="button" class="tabledit-save-button btn btn-xs btn-outline-secondary jqt-small-button" style="display: none; float: none;">Kaydet</button>
                                            <button type="button" class="tabledit-confirm-button btn btn-xs btn-danger jqt-small-button" style="display: none; float: none;">Onayla</button>
                                        </div>
                                        </td>
                                    </tr>`
                }
            });

            // datatables render
            dt2TeIpRels.api.dataTables.renderTable();

            // on datatables draw.dt event, render edit table
            dt2TeIpRels.api.dataTables.attachDrawDt();

        }
    };

    $(document).ready(function () {

        // FIELDS TABLE
        Step3.showDocFieldsTable();

        // IP RELATIONS TABLE
        Step3.showIpRelationsTable();

    }); // ready

})(jQuery);