var bindings = [],
    bindingsEnabled = false,
    OTHER_TEXT = getLocalizedText('otherCard') || "Другая карта...",
    isOpenAutocomplete = false,
    eventsValidation = "keydown keyup paste";

$.ui.keyCode.ZERO = 48;
$.ui.keyCode.NINE = 57;

// show/hide CVC code
$(document).on('click', '.opener-code span', function(event) {
    event.preventDefault();
    if ($('#iCVC').get(0).type == 'password') {
        $('#iCVC').get(0).type = 'text'
        $('.opener-code').toggleClass('active');
    } else {
        $('#iCVC').get(0).type = 'password';
        $('.opener-code').toggleClass('active');
    }
});

function initSelect() {
    if ($('.select').size() && !navigator.userAgent.match(/iPhone|iPad|iPod/i)) {
        var select = $('select.select').selectmenu();
    }
};

if (/(Windows Phone|iemobile|WPDesktop)/i.test(navigator.userAgent)) { //iemobile doesn't support normal keydown event
    eventsValidation = "keyup paste";
}

$(document)  //Validation pan
    .on(eventsValidation, '#pan_visible', function(event){
        var target = event.target,
            position = target.selectionEnd,
            length = target.value.length;
        var key = window.event ? event.keyCode : event.which;
        if (($("#combobox option").size() > 0) && ($("#combobox").val() !== 'other') && (key == $.ui.keyCode.DOWN || key == $.ui.keyCode.UP)) {
            if (!isOpenAutocomplete) {
                $(this).focus();
                $(this).autocomplete("search", "");
                isOpenAutocomplete = true;
            }
        } else if (($("#combobox option").size() > 0) && ($("#combobox").val() !== 'other') &&
                   (key >= $.ui.keyCode.ZERO && key <= $.ui.keyCode.NINE || key == $.ui.keyCode.DELETE || key == $.ui.keyCode.BACKSPACE)) {
            return false;
        }

        if (key == $.ui.keyCode.DOWN || key == $.ui.keyCode.UP || key == $.ui.keyCode.ENTER || $("#combobox").val() !== 'other'){
            $(this).val($(this).val().replace(/[^\d\*]/g, '').replace(/([\d\*]{4}(?!$))/g,'$1 '));
        } else if (key == $.ui.keyCode.LEFT || key == $.ui.keyCode.RIGHT || key == $.ui.keyCode.DELETE || key == $.ui.keyCode.BACKSPACE) {
            return true;
        } else {
            $(this).val($(this).val().replace(/[^\d]/g, '').replace(/(\d{4}(?!$))/g,'$1 '));
        }

        target.selectionEnd = position += ((target.value.charAt(position - 1) === ' ' &&
                                            target.value.charAt(length - 1) === ' ' &&
                                            length !== target.value.length) ? 1 : 0);

    })
    .on('autocompletechange', '#pan_visible', function(){
        $(this).val($(this).val().replace(/[^\d\*]/g, '').replace(/([\d\*]{4}(?!$))/g,'$1 '));
    });

function initBindings(){
    $("#combobox").change(function(){
        setEnableBinding(selectdItem.value != OTHER_TEXT);
    });
    $("#iCVC").keyup(function(value){
        $("#bindingCvc").val($("#iCVC").val());
    });
    $("#formBinding").hide();
    $("#buttonBindingPayment2").hide();
    $("#buttonBindingPayment2").click(function(){
        if (!$("#buttonBindingPayment").attr('disabled')) {
            $('#buttonBindingPayment').click();
        }
    });
    if ($("#bindingIdSelect option").size() > 0) {
        $('#combobox').append('<option value="other">'+OTHER_TEXT+'</option>');
    }
    $("#bindingIdSelect option").each(function(index, element){
        if (element.text.length > 0){
            var binding = {value:'',month:'',year:''};
            var len = element.text.length;
            var pan = element.text.substr(0, len-6);
            binding.value = element.value;
            binding.month = element.text.substr(len-5, 2);
            binding.year = element.text.substr(len-2, 2);
            bindings[pan] = binding;
            element.text = pan;
        }
        $('#combobox').append("<option value='"+element.value+"'>"+element.text+"</option>");
    });
}

function setEnableBinding(enable){
    $("#pan_visible").val("");
    isOpenAutocomplete = false;
    if (enable){
        //ENABLE bindings
        clearErrorsView();
        bindingsEnabled = true;
        $("#buttonPayment").hide();
        $("#buttonBindingPayment2").show();
        $("#bindingIdSelect [value='"+bindings[selectdItem.value].value+"']").attr("selected", "selected");
        $("#month").val(bindings[selectdItem.value].month);
        $("#year").val("20" + bindings[selectdItem.value].year);
        $(".select-month .ui-selectmenu-status").text($("#month option[value='"+bindings[selectdItem.value].month+"']").text());
        $(".select-year .ui-selectmenu-status").text($("#year option[value='20"+bindings[selectdItem.value].year+"']").text());
        $("#iTEXT").val("");
        document.getElementById("month").disabled=true;
        document.getElementById("year").disabled=true;
        document.getElementById("iTEXT").disabled=true;
        $("#delete-binding").show();
        $("#month").selectmenu().selectmenu('refresh', true);
        $("#year").selectmenu().selectmenu('refresh', true);
    } else {
        //DISABLE bindings
        bindingsEnabled = false;
        $("#buttonPayment").show();
        $("#buttonBindingPayment2").hide();
        $("#bindingIdSelect option:selected").attr("selected", false);
        document.getElementById("iTEXT").disabled=false;
        document.getElementById("month").disabled=false;
        document.getElementById("year").disabled=false;
        $("#delete-binding").hide();
        $("#month").selectmenu().selectmenu('refresh', true);
        $("#year").selectmenu().selectmenu('refresh', true);
    }
}

var errorFields = [
    {id:'#iTEXT',         borderId:'.name-card',        message: getLocalizedText('err_cardholder')},
    {id:'#iPAN',          borderId:'.number-selection', message: getLocalizedText('err_pan')},
    {id:'#iCVC',          borderId:'.code',             message: getLocalizedText('err_cvc')},
    {id:'#year',          borderId:'.choice-date',      message: getLocalizedText('err_expiry')},
    {id:'#agreeCheckbox', borderId:'.agreeBox',         message: getLocalizedText('err_agreement')},
    {id:'#email',         borderId:'.mail-wrap',        message: getLocalizedText('err_email')}
];

function updateErrors(){
    $('#errorBlock1').empty();
    errorFields.forEach(function(element){
        if ($(element.id).hasClass("invalid")){
            $(element.borderId).addClass('error');
            $('#errorBlock1').append('<p class = "errorField">'+element.message+'</p>');
        } else {
            $(element.borderId).removeClass('error');
        }
    });
}

function clearErrorsView(){
    $('#errorBlock').empty();
    $('#errorBlock1').empty();
    errorFields.forEach(function(element){
        $(element.borderId).removeClass('error');
    });
}

// legacy
function togglePasswordField(elem){
    if ($(elem).get(0).type == 'password') {
        $(elem).get(0).type = 'text'
        $('.opener-code').toggleClass('active');
    } else {
        $(elem).get(0).type = 'password';
        $('.opener-code').toggleClass('active');
    }
}