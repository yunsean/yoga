function message(type, content, hidden) {
  new $.Display({
    display: 'messager'
  }).show({
    content: content,
    type: type,
    placement: 'top-center',
    autoHide: 3000,
    hidden: hidden
  });
}

function warning(content) {
  message("warning", content, null)
}
function success(content) {
  message("success", content, null)
}
function info(content) {
  message("info", content, null)
}

function serialize(form) {
  var formData = {};
  var a = $(form).serializeArray();
  console.log(a);
  $.each(a, function () {
    formData[this.name] = this.value;
  });
  return formData;
}