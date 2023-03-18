
/************************************************************

    Common lib

*************************************************************/

var Common = Common || {};

!(function ($) {
    'use strict';

    Common = {
        attachLoadingUiEvents: function () {
            $(document).ajaxStart(function () {
                Common.showLoading();
            });
            $(document).ajaxSuccess(function (event, xhr, settings) {
                Common.hideLoading();
            });
            $(document).ajaxError(function (event, xhr, settings, thrownError) {
                Common.hideLoading();
            });
        },
        showLoading: function (selector) {
            if (!selector)
                selector = 'body';

            $(selector).block({
                message: '',
                fadeIn: 100,
                css: {
                    border: 'none',
                    padding: '15px',
                    backgroundColor: '#000',
                    '-webkit-border-radius': '10px',
                    '-moz-border-radius': '10px',
                    opacity: 0.3,
                    color: '#fff'
                }
            });
        },
        hideLoading: function (selector) {
            if (!selector)
                selector = 'body';
            $(selector).unblock({
                fadeOut: 200
            });
        },
        // status: success, warning, error, info
        showNotif: function (status, title, text) {
            new Notify({
                title: title,
                text: text,
                effect: 'slide',
                autoclose: true,
                autotimeout: 4000,
                status: status,
                showCloseButton: true,
                showIcon: true,
                position: 'right top'
            });
        }
    };

    $(document).ready(function () {

        // show/hide loading on every ajax req. 
        Common.attachLoadingUiEvents();

    });

})(jQuery);