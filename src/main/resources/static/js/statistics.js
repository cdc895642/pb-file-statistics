$(document).ready(function () {
  $("#load-btn").click(function (event) {
    event.preventDefault();
    addLoadButtonHandler();
  });

});

function addLoadButtonHandler() {
  if (!$('#file-input').val().length){
    $("#result").text("file is not selected");
    return;
  }
  let form = $('#load-file-form')[0];
  let data = new FormData(form);

  $.ajax({
    type: "POST",
    enctype: 'multipart/form-data',
    url: "/getStatistics",
    data: data,
    processData: false, //prevent jQuery from automatically transforming the data into a query string
    contentType: false,
    cache: false,
    timeout: 60000,
    success: function (data) {
      $("#result").text("file has been uploaded and processed successfully");
      console.log("SUCCESS : ", data);
      fillTable(data);
    },
    error: function (e) {
      let err=e.responseJSON;
      $("#result").text(err["message"]);
      console.log("ERROR : ", e);
    }
  });
}

function fillTable(data) {
  $("#stat-table tbody tr").remove();
  $.each(JSON.parse(JSON.stringify(data)), function (i, item) {
    $('<tr>').append(
        $('<td>').text(item.fileName),
        $('<td>').text(item.linesCount),
        $('<td>').text(item.averageWordsCountInLine),
        $('<td>').text(item.minWordLength),
        $('<td>').text(item.maxWordLength)
    ).appendTo('#stat-table tbody');
  });
}