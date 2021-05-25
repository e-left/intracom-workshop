import "./intracom.scss";
import bootstrap from "bootstrap";
import $ from "jquery";

const rootUrl = "https://web-e-left.cloud.okteto.net/";

const eNodeBDropdown = $("#enodeb");
const cellDropdown = $("#cell");
const tbody = $("#tbody-logs");

$.ajax({
  url: rootUrl + "/enodeb",
  type: "GET",
  crossDomain: true,
  dataType: "json",
  success: function (result) {
    tbody.empty();
    $.each(result, function () {
      eNodeBDropdown.append($("<option />").val(this).text(this));
    });
  },
});

eNodeBDropdown.on("change", function () {
  $.ajax({
    url: rootUrl + "/cells/" + eNodeBDropdown.val(),
    type: "GET",
    crossDomain: true,
    dataType: "json",
    success: function (result) {
      cellDropdown.empty();
      cellDropdown.append("<option value=\"\" selected disabled>Please select a Cell</option>");
      tbody.empty();
      cellDropdown.prop("disabled", false);
      $.each(result, function () {
        cellDropdown.append($("<option />").val(this).text(this));
      });
    },
  });

  $.ajax({
    contentType: "application/json",
    url: rootUrl + "/logs",
    type: "POST",
    data: JSON.stringify({
      eNodeb: eNodeBDropdown.val(),
    }),
    crossDomain: true,
    dataType: "json",
    success: function (result) {
      tbody.empty();
      $.each(result, function () {
        let elements = [
          this.recordDate,
          this.eNodeB,
          this.cell,
          this.cellAvailability,
          this.CQI,
          this.drops,
          this.accessibility,
          this.retainability,
        ];
        let appendString = "";
        for(e of elements) {
            appendString += "<td>" + e + "</td>"
        }
        tbody.append("<tr>" + appendString + "</tr>");
      });
    },
  });

});

cellDropdown.on("change", function () {
  $.ajax({
    contentType: "application/json",
    url: rootUrl + "/logs",
    type: "POST",
    data: JSON.stringify({
      eNodeb: eNodeBDropdown.val(),
      cell: cellDropdown.val(),
    }),
    crossDomain: true,
    dataType: "json",
    success: function (result) {
      tbody.empty();
      $.each(result, function () {
        let elements = [
          this.recordDate,
          this.eNodeB,
          this.cell,
          this.cellAvailability,
          this.CQI,
          this.drops,
          this.accessibility,
          this.retainability,
        ];
        let appendString = "";
        for(e of elements) {
            appendString += "<td>" + e + "</td>"
        }
        tbody.append("<tr>" + appendString + "</tr>");
      });
    },
  });
});
