//If you are using Bootstrap version 3, include this line somewhere before your first notice:
PNotify.prototype.options.styling = "bootstrap3";

PNotify.myTips = function (title, content, type, timeout) {
    type = type || 'notice';
    timeout = timeout || 3000;
    new PNotify({
        title: title,
        text: content,
        type: type,
        delay: timeout,
        animate: {
            animate: true,
            in_class: 'slideInDown',
            out_class: 'slideOutUp'
        }
    });
};