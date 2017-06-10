//code obtained from https://jqueryui.com/datepicker/#min-max
//Date matches Booking Day dates (Jan 1st, 2007 to December 31, 2020)
$( function() {
  $( "#datepicker" ).datepicker({ minDate: new Date(2007, 1,1), maxDate: new Date(2020, 12,31)});
} );