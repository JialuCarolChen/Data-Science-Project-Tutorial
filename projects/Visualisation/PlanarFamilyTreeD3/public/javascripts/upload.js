var hostAddress = window.location.protocol + '//' + window.location.host;

$('.upload-btn').on('click', function (){
    $('#upload-input').click();
    $('.progress-bar').text('0%');
    $('.progress-bar').width('0%');
});

$('#viewAllImg').on('click', function(){
      $.ajax({
      url: '/getImgList',
      type: 'get',
      success: function(data){
        ImgURLs = JSON.parse(data);
        let htmlcode = '';
        ImgURLs.map(function(ImgURL){
          htmlcode += '<a href='+ hostAddress + ImgURL+'>' + hostAddress + ImgURL + '</a><br />';
        })
        $('#imgList').html(htmlcode);
      },
      error: function(){
        alert('Get Images list failed.');
      },
    });
})

$('#upload-input').on('change', function(){

  var files = $(this).get(0).files;

  if (files.length > 0){
    // create a FormData object which will be sent as the data payload in the
    // AJAX request
    var formData = new FormData();

    // loop through all the selected files and add them to the formData object
    for (var i = 0; i < files.length; i++) {
      var file = files[i];

      // add the files to formData object for the data payload
      formData.append('uploads[]', file, file.name);
    }

    $.ajax({
      url: '/upload',
      type: 'POST',
      data: formData,
      processData: false,
      contentType: false,
      success: function(jsonData){
        let data = JSON.parse(jsonData);
        if (data[1] == true) {
          $('.progress-bar').html('This image already exists.');
        } else {
          $('.progress-bar').html('Success');
          $('#imgList').prepend('<a href='+hostAddress + data[0]+'>' + hostAddress + data[0] + '</a><br />');
        }
        $('#imgLink-container').show();
        $('#imgLink').html('<a href='+hostAddress + data[0]+'>' + hostAddress + data[0] + '</a>');
      },
      error: function(){
        alert('Upload failed.');
      },
      xhr: function() {
        // create an XMLHttpRequest
        var xhr = new XMLHttpRequest();

        // listen to the 'progress' event
        xhr.upload.addEventListener('progress', function(evt) {

          if (evt.lengthComputable) {
            // calculate the percentage of upload completed
            var percentComplete = evt.loaded / evt.total;
            percentComplete = parseInt(percentComplete * 100);

            // update the Bootstrap progress bar with the new percentage
            $('.progress-bar').text(percentComplete + '%');
            $('.progress-bar').width(percentComplete + '%');

            // once the upload reaches 100%, set the progress bar text to done
            if (percentComplete === 100) {
              $('.progress-bar').html('Done');
            }

          }

        }, false);

        return xhr;
      }
    });

  }
});
