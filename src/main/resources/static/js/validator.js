function sumOn(a, b) {
	return a + b;
}

const tabulatorRegistry = {};
var sessionFlag = false;
var initLoad = true;

let commonData = [];
// Function to store a value with a key
function storeData(key, value) {
	// Check if the key already exists
	const existingEntry = commonData.find(entry => entry.key === key);
	if (existingEntry) {
		// Update the value if the key already exists
		existingEntry.value = value;
	} else {
		// Add a new entry if the key does not exist
		commonData.push({ key: key, value: value });
	}
}
// Function to retrieve a value by key
function retrieveData(key) {
	const entry = commonData.find(entry => entry.key === key);
	return entry ? entry.value : null; // Return null if the key does not exist
}

function capitalizeFirst(str) {
  if (!str || typeof str !== 'string') return str;  // Handle edge cases (empty or non-string)
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();  // Optional: .toLowerCase() on the rest for consistency
}

function setInputLength(table, fieldName, maxLength) {

	const column = table.getColumn(fieldName);
	if (column) {
		const editor = column.getDefinition().editor;
		if (editor) {
			const params = column.getDefinition().editorParams || {};
			params.maxLength = maxLength;
			column.updateDefinition({ editorParams: params });
		}
	}
}

function setValueTab(rowNumber, columnField, valueToSet) {
	// Get the row by its index (rowNumber - 1 because rowNumber is 1-based)

	//console.log('Here final check : ' + rowNumber + ' || ' + columnField + ' || ' + valueToSet);

	if (valueToSet == null) {
		valueToSet = '';
	}

	var row = table.getRow(rowNumber);

	// Check if the row exists
	if (row) {
		//alert(columnField + ' and ' + valueToSet + ' and ' + rowNumber);
		// Update the specific field in the row
		row.update({ [columnField]: valueToSet });
	} else {
		console.error("Row not found");
	}
}

function addClassToColumnInputs(table, fieldName, className) {
	function addClass() {
		const cells = table.element.querySelectorAll(
			`.tabulator-cell[data-tabulator="${fieldName}"] input.tab_fld`
		);
		cells.forEach(input => input.classList.add(className));
	}
	table.on("tableBuilt", addClass);
	table.on("dataLoaded", addClass);
}

function checkMailInputs(table) {
	let count = 0;

	// Iterate through all rows
	table.getRows().forEach(row => {
		const cell = row.getCell('typeOfAnimal');
		if (cell && cell.getElement().querySelector('.mymail')) {
			cell.getElement().querySelector('.mymail').classList.add('mytestUltra');
			count++;
		}
	});

	console.log('Mail inputs found:', count);
	return count;
}

function chkToday(str) {
	if (str == 'today') {
		const d = new Date();
		let year = d.getFullYear();
		let month = d.getMonth() + 1;
		let date = d.getDate();
		str = padDigits(month, 2) + "/" + padDigits(date, 2) + "/" + padDigits(year, 4);
	}

	console.log('str check ' + str);

	return str;
}

function padDigits(number, digits) {
	return Array(Math.max(digits - String(number).length + 1, 0)).join(0) + number;
}

function differDay(dt, diff) {
	var day = new Date(dt);

	var nd = new Date(day);
	if (diff == 'N') {
		nd.setDate(day.getDate() + 1);
	} else {
		nd.setDate(day.getDate() - 1);
	}

	let year = nd.getFullYear();
	let month = nd.getMonth() + 1;
	let date = nd.getDate();
	var str = padDigits(month, 2) + "/" + padDigits(date, 2) + "/" + padDigits(year, 4);
	return str;
}

function dateCheck(dt, fld, opt) {
	//alert(dt + ' and ' + fld);
	if (opt == 0) {
		var date_check = $('#' + fld).attr('data-check');
	} else {
		var date_check = fld;
	}

	var ret_msg = '';

	//alert(date_check);

	const d = new Date();
	let year = d.getFullYear();
	let month = d.getMonth() + 1;
	let date = d.getDate();
	var today = padDigits(month, 2) + "/" + padDigits(date, 2) + "/" + padDigits(year, 4);

	if (dt == today) {
		/*if(date_check.includes('not:today') || date_check.includes('maxdt-:today') || date_check.includes('mindt+:today')) {
			ret_msg = 'You cannot able to enter today date';
		}*/

		if (date_check.includes('not:today')) {

			//console.log('Test: ' + date_check.split('not:today')[1].split(',')[0].split('|||')[1]);

			if (date_check.split('not:today')[1].split(',')[0].split('|||')[1] != '') {
				ret_msg = date_check.split('not:today')[1].split(',')[0].split('|||')[1];
			} else {
				ret_msg = 'You cannot able to enter today date';
			}

		} else if (date_check.includes('maxdt-:today')) {

			if (date_check.split('maxdt-:today')[1].split(',')[0].split('|||')[1] != '') {
				ret_msg = date_check.split('maxdt-:today')[1].split(',')[0].split('|||')[1];
			} else {
				ret_msg = 'You cannot able to enter today date';
			}

		} else if (date_check.includes('mindt+:today')) {

			if (date_check.split('mindt+:today')[1].split(',')[0].split('|||')[1] != '') {
				ret_msg = date_check.split('mindt+:today')[1].split(',')[0].split('|||')[1];
			} else {
				ret_msg = 'You cannot able to enter today date';
			}

		}

	}

	var d1 = new Date(dt.split('/')[2], dt.split('/')[0] - 1, dt.split('/')[1]);

	if (d1 > d) {
		/*if (date_check.includes('maxdt-:today') || date_check.includes('maxdt:today')) {
			ret_msg = 'You cannot able to enter future date';
		}*/

		if (date_check.includes('maxdt-:today')) {

			if (date_check.split('maxdt-:today')[1].split(',')[0].split('|||')[1] != '') {
				ret_msg = date_check.split('maxdt-:today')[1].split(',')[0].split('|||')[1];
			} else {
				ret_msg = 'You cannot able to enter future date';
			}

		} else if (date_check.includes('maxdt:today')) {

			if (date_check.split('maxdt:today')[1].split(',')[0].split('|||')[1] != '') {
				ret_msg = date_check.split('maxdt:today')[1].split(',')[0].split('|||')[1];
			} else {
				ret_msg = 'You cannot able to enter future date';
			}

		}
	}

	if (d1 < d) {
		/*if (date_check.includes('mindt+:today') || date_check.includes('mindt:today')) {
			ret_msg = 'You cannot able to enter past date';
		}*/

		if (date_check.includes('mindt+:today')) {

			if (date_check.split('mindt+:today')[1].split(',')[0].split('|||')[1] != '') {
				ret_msg = date_check.split('mindt+:today')[1].split(',')[0].split('|||')[1];
			} else {
				ret_msg = 'You cannot able to enter past date';
			}

		} else if (date_check.includes('mindt:today')) {

			if (date_check.split('mindt:today')[1].split(',')[0].split('|||')[1] != '') {
				ret_msg = date_check.split('mindt:today')[1].split(',')[0].split('|||')[1];
			} else {
				ret_msg = 'You cannot able to enter past date';
			}

		}

	}

	return ret_msg;

}

function checkAvailable(dt, fld, opt) {

	if (opt == 0) {
		var valid_dt = $('#' + fld).attr('data-valid');
	} else {
		var valid_dt = fld;
	}

	var mindt = valid_dt.split('::')[0];
	var maxdt = valid_dt.split('::')[1];
	var ddates = valid_dt.split('::')[2].split(',');

	if (mindt != '') {
		var d1 = new Date(dt.split('/')[2], dt.split('/')[0] - 1, dt.split('/')[1]);
		var d2 = new Date(mindt.split('/')[2], mindt.split('/')[0] - 1, mindt.split('/')[1]);

		if (d1 < d2) {
			return false;
		}

	}

	if (maxdt != '') {
		var d1 = new Date(dt.split('/')[2], dt.split('/')[0] - 1, dt.split('/')[1]);
		var d2 = new Date(maxdt.split('/')[2], maxdt.split('/')[0] - 1, maxdt.split('/')[1]);

		if (d1 > d2) {
			return false;
		}

	}

	for (var i = 0; i < ddates.length; i++) {
		if (dt == ddates[i]) {
			return false;
		}
	}

	return true;

}

function valDate(mydate) {
	var valid = true;
	if (mydate.indexOf("/") == -1) {
		valid = false;
	}
	if (mydate.split('/').length != 3) {
		valid = false;
	}
	var mn = parseInt(mydate.split('/')[0]);
	var dt = parseInt(mydate.split('/')[1]);
	var yr = parseInt(mydate.split('/')[2]);
	if (mn > 12 || mn < 1) {
		valid = false;
	}
	if (mn == 1 || mn == 3 || mn == 5 || mn == 7 || mn == 8 || mn == 10 || mn == 12) {
		if (dt > 31 || dt < 1) {
			valid = false;
		}
	}
	if (mn == 4 || mn == 6 || mn == 9 || mn == 11) {
		if (dt > 30 || dt < 1) {
			valid = false;
		}
	}
	if (mn == 2) {
		if (yr % 4 == 0) {
			if (dt > 29 || dt < 1) {
				valid = false;
			}
		} else {
			if (dt > 28 || dt < 1) {
				valid = false;
			}
		}
	}
	if (yr > 2100 || yr < 1900) {
		valid = false;
	}
	return valid;
}

function findWordInString(str, word) {
	const regex = new RegExp(`\\b${word}\\b`, 'i'); // 'i' for case-insensitive search
	return regex.test(str);
}

function getHeaderAttrByTbl(table, field, attrName) {
	// Get the column definition for the specified field
	const column = table.getColumn(field);
	const headerEl = column.getElement();
	// Check if the column exists
	if (column) {
		// Return the specified attribute of the column
		const titleEl = headerEl.querySelector('.tabulator-col-title');
		return titleEl ? titleEl.getAttribute(attrName) : null;
	} else {
		console.error(`Column with field "${field}" not found.`);
		return null;
	}
}

// Get header attribute from cell
function getHeaderAttribute(cell, attrName) {
	const column = cell.getColumn();
	const headerEl = column.getElement();
	if (!headerEl) {
		log(`Warning: Header element not found for column ${column.getField()}`);
		return null;
	}
	const titleEl = headerEl.querySelector('.tabulator-col-title');
	return titleEl ? titleEl.getAttribute(attrName) : null;
}

// Custom input editor with maxLength
var customInputEditor = function(cell) {
	// Create an input element
	const input = document.createElement("input");
	input.type = "text";
	input.value = cell.getValue();
	input.maxLength = cell.getColumn().getDefinition().editorParams.maxLength || 255; // Set maxLength

	// Add event listener to update cell value on change
	input.addEventListener("change", function() {
		cell.setValue(input.value);
	});
	// Return the input element
	return input;
};

function clearSBI() {
	$.ajax({
		url: contextPath + 'clrSBI',
		method: 'POST',
		success: function(response) {
			console.log('sessionCleared');
		},
		error: function(error) {
			//console.error("Error getting table names:", error);
		}
	});
}

var dateEditor = function(cell, onRendered, success, cancel) {
	var input = document.createElement("input");
	input.type = "text";
	input.value = cell.getValue() || "";
	input.setAttribute("maxlength", "10");
	input.style.padding = "4px";
	input.style.width = "100%";

	$(input).datepicker({
		dateFormat: "mm/dd/yy",
		changeMonth: true,
		changeYear: true,
		yearRange: (function() {
			var currentYear = new Date().getFullYear();
			return (currentYear - 20) + ":" + (currentYear + 20);
		})(),
		onSelect: function(dateText) {
			success(dateText);

			if (getHeaderAttribute(cell, 'data-check') != null) {
				var dataCheck = getHeaderAttribute(cell, 'data-check');
				var dataValid = getHeaderAttribute(cell, 'data-valid');
				datePickerCheck(cell, dateText, dataCheck, dataValid);
			}

			$(input).datepicker("hide");
		}
	});



	// Automatically insert slashes
	input.addEventListener("input", function(e) {
		let value = input.value.replace(/\D/g, ""); // Remove all non-digits
		if (value.length > 2 && value.length <= 4) {
			value = value.slice(0, 2) + "/" + value.slice(2);
		} else if (value.length > 4) {
			value = value.slice(0, 2) + "/" + value.slice(2, 4) + "/" + value.slice(4, 8);
		}
		input.value = value;
	});

	input.addEventListener("keydown", function(e) {
		const dateText = input.value.trim();
		const isValid = /^\d{2}\/\d{2}\/\d{4}$/.test(dateText);

		if (e.key === "Enter" || e.key === "Tab") {
			e.preventDefault();
			if (isValid) {
				success(dateText);
			} else {
				cancel();
			}
		} else if (e.key === "Escape") {
			cancel();
		}
	});

	onRendered(function() {
		input.focus();
		input.select();
	});

	return input;
};

function datePickerCheck(cell, dateText, dataCheck, dataValid) {

	//alert(dateText + ' and ' + dataCheck + ' and ' + dataValid);

	if (dateText != '') {
		var mydate = dateText;
		if (!valDate(mydate)) {
			cell.setValue('');
			message('CUW', 'Date is not valid', 0);
			//alert("Invalid Date");
			//Swal.fire('Alert', 'Date is not valid', 'warning');

		} else {
			var newdt = padDigits(mydate.split("/")[0], 2) + "/" + padDigits(mydate.split("/")[1], 2) + "/" + mydate.split("/")[2];
			if (dateCheck(newdt, dataCheck, 1) != '') {
				cell.setValue('');
				var msg = dateCheck(newdt, dataCheck, 1);
				//Swal.fire('Alert', msg, 'warning');
				message('CUW', msg, 0);
				return;
			}

			if (checkAvailable(newdt, dataValid, 1)) {
				cell.setValue(newdt);
			} else {

				cell.setValue('');

				var date_check = dataCheck;

				date_rules = date_check.split(',');

				for (var i = 0; i < date_rules.length; i++) {

					if (!date_rules[i].includes('today')) {
						//console.log('here some specific : ' + date_rules[i]);

						if (date_rules[i].includes('not:')) {
							var getdt = date_rules[i].split("not:")[1].split("|||")[0];
							var getmsg = date_rules[i].split("not:")[1].split("|||")[1];
							if (newdt == getdt) {
								if (getmsg != '') {
									//Swal.fire('Alert', getmsg, 'warning');
									message('CUW', getmsg, 0);
									return;
								}
							}
						}

						if (date_rules[i].includes('maxdt-:')) {
							var getdt = date_rules[i].split("maxdt-:")[1].split("|||")[0];
							var getmsg = date_rules[i].split("maxdt-:")[1].split("|||")[1];

							var d1 = new Date(newdt.split('/')[2], newdt.split('/')[0] - 1, newdt.split('/')[1]);
							var d2 = new Date(getdt.split('/')[2], getdt.split('/')[0] - 1, getdt.split('/')[1]);

							if (newdt == getdt || d1 > d2) {
								if (getmsg != '') {
									//Swal.fire('Alert', getmsg, 'warning');
									message('CUW', getmsg, 0);
									return;
								}
							}
						}

						if (date_rules[i].includes('mindt+:')) {
							var getdt = date_rules[i].split("mindt+:")[1].split("|||")[0];
							var getmsg = date_rules[i].split("mindt+:")[1].split("|||")[1];

							var d1 = new Date(newdt.split('/')[2], newdt.split('/')[0] - 1, newdt.split('/')[1]);
							var d2 = new Date(getdt.split('/')[2], getdt.split('/')[0] - 1, getdt.split('/')[1]);

							if (newdt == getdt || d1 < d2) {
								if (getmsg != '') {
									//Swal.fire('Alert', getmsg, 'warning');
									message('CUW', getmsg, 0);
									return;
								}
							}
						}

						if (date_rules[i].includes('maxdt:')) {
							var getdt = date_rules[i].split("maxdt:")[1].split("|||")[0];
							var getmsg = date_rules[i].split("maxdt:")[1].split("|||")[1];

							var d1 = new Date(newdt.split('/')[2], newdt.split('/')[0] - 1, newdt.split('/')[1]);
							var d2 = new Date(getdt.split('/')[2], getdt.split('/')[0] - 1, getdt.split('/')[1]);

							if (d1 > d2) {
								if (getmsg != '') {
									//Swal.fire('Alert', getmsg, 'warning');
									message('CUW', getmsg, 0);
									return;
								}
							}
						}

						if (date_rules[i].includes('mindt:')) {
							var getdt = date_rules[i].split("mindt:")[1].split("|||")[0];
							var getmsg = date_rules[i].split("mindt:")[1].split("|||")[1];

							console.log('test alpha : ' + newdt);
							console.log('test beta : ' + getdt);

							var d1 = new Date(newdt.split('/')[2], newdt.split('/')[0] - 1, newdt.split('/')[1]);
							var d2 = new Date(getdt.split('/')[2], getdt.split('/')[0] - 1, getdt.split('/')[1]);

							if (d1 < d2) {
								if (getmsg != '') {
									//Swal.fire('Alert', getmsg, 'warning');
									message('CUW', getmsg, 0);
									return;
								}
							}
						}


					}

				}

				//alert("Disabled Date Not Allowed");
				//Swal.fire('Alert', 'You cannot enter this date ' + newdt, 'warning');
				message('CUW', 'You cannot enter this date ' + newdt, 0);
			}

		}
	}

}


storeData('baduid', 'BADMMRP');
storeData('dacsuid', 'DACS');

function setTitleDesc(column, field, label) {
	column.getElement().classList.add("rawField");
	column.getElement().setAttribute('data-fld', field);
	column.getElement().setAttribute('data-lbl', label);
	return label;
}

function setField(cell) {
	return `<input type="text" name="myfld" class="tab_fld" />`;
}

//var loaderprogress = 0;
pro_loader();

function pro_loader() {
	var loaderprogress = 0;

	$('#loader_title').text('Loading...');
	const loadprogressBar = document.getElementById("commonPerprogressBar");
	const loader = document.getElementById("commonPerloader");

	const interval = setInterval(() => {

		if (loaderprogress < 100) {
			loaderprogress++;
			loadprogressBar.style.width = loaderprogress + "%";
			loadprogressBar.textContent = loaderprogress + "%";
		} else if (loaderprogress == 100) {
			clearInterval(interval);
			$('#loader_title').text('Progressing...');
		}

		/*if (loaderprogress >= 100) {
			clearInterval(interval);
		}*/

	}, 40);

}

function pro_loader2() {
	//$('#loader_title').text('Loading...');
	var progress = setInterval(function() {
		var $bar = $("#bar");

		if ($bar.width() >= 500) {
			clearInterval(progress);
		} else {
			$bar.width($bar.width() + 100);
		}
		$('#loader_title').text('Loading...');
		$bar.text(parseInt($bar.width() / 5) + "%");
		if ($bar.width() / 5 == 100) {
			$('#loader_title').text('Progressing...');
			$bar.text(parseInt($bar.width() / 5) + "%");
		}
	}, 1000);
}

function loader(x) {
	if (x == 1) {
		//loaderprogress = 0;
		if ($('#commonPerprogressBar').text() == '0%') {
			var loaderprogress = 0;
			$('#commonPerprogressBar').css('width', loaderprogress + "%");
			$('#commonPerprogressBar').text(loaderprogress + "%");
			setTimeout(function() {
				pro_loader();
				$('#loading-wrapperx').show();
			}, 500);
		}
	} else if (x == 0) {
		$('#loading-wrapperx').fadeOut();
		$('#commonPerprogressBar').text('0%');
	}
}

/*function loader(x) {
	if (x == 1) {
		//$('#loading-wrapperx').show();
		loaderprogress = 0;
		$('#commonPerprogressBar').css('width', loaderprogress + "%");
		$('#commonPerprogressBar').text(loaderprogress + "%");
		setTimeout(function() {
			pro_loader();
			$('#commonPerloader').show();
		}, 500);
	} else if (x == 0) {
		//$('#loading-wrapperx').hide();
		loaderprogress = 0;
		$('#commonPerloader').hide();
	}
}*/

function loader_start(loaderprogress) {
	/*if (x == 1) {
		$('#loading-wrapperx').show();
	} else if (x == 0) {
		$('#loading-wrapperx').hide();
	}*/

	//if (x == 1) {
	$('#loader_title').text('Loading...');
	const loadprogressBar = document.getElementById("commonPerprogressBar");
	const loader = document.getElementById("commonPerloader");
	const loadermainContent = document.getElementById("loadermainContent");

	const interval = setInterval(() => {
		loaderprogress++;
		loadprogressBar.style.width = loaderprogress + "%";
		loadprogressBar.textContent = loaderprogress + "%";

		if (loaderprogress >= 100) {
			clearInterval(interval);
			//loader.style.display = "none";
			//loadermainContent.style.display = "none";

			// Your code to run after loader completes
			// For example:
			//initializeMainContent();
		}

		if (loaderprogress == 100) {
			$('#loader_title').text('Still working...');
		}
	}, 40); // 40ms * 100 = 4 seconds

	//} 


	/*else {
		loader.style.display = "none";
		loadermainContent.style.display = "none";
	}*/


}

function focusCell(tbl, rowId, fieldName) {
	if (!tbl || !tbl.getRow) {
		console.error("Invalid Tabulator instance", tbl);
		return;
	}
	const row = tbl.getRow(rowId);
	if (row) {
		const cell = row.getCell(fieldName);
		if (cell) {
			cell.focus();
			console.log("Focused cell:", fieldName, "in row:", rowId);
		} else {
			console.warn("Cell not found in row:", rowId, "field:", fieldName);
		}
	} else {
		console.warn("Row not found with ID:", rowId);
	}
}

function focusField(tbl, rowId, field) {
	rowId = parseInt(rowId);
	var row = tbl.getRow(rowId); // Get the row by ID
	if (row) {
		var cell = row.getCell(field); // Get the cell from the row
		if (cell) {
			cell.edit(); // Open the editor for the cell
		} else {
			console.error("Cell not found for field:", field);
		}
	} else {
		console.error("Row not found for rowId:", rowId);
	}
}

function getCellVal(tbl, rowId, field) {
	// Get the row by its ID
	var row = tbl.getRow(rowId);

	// Check if the row exists
	if (row) {
		// Get the cell value
		var cell = row.getCell(field);
		return cell ? cell.getValue() : ''; // Return the cell value or null if the cell doesn't exist
	} else {
		console.error("Row not found");
		return null; // Return null if the row doesn't exist
	}
}

function setAttributeForTabulatorCells(tbl, fieldName, valid, rul) {

	tbl.getRows().forEach(row => {
		console.log('as test : ' + fieldName);
		const cell = row.getCell(fieldName);
		if (cell) {
			console.log('inside');
			cell.getElement().setAttribute('data-valid', valid);
			cell.getElement().setAttribute('data-check', rul);
		}
	});

	//alert(tbl.element);	
	/*const cells = tbl.getElement().getCell(field);
	cells.forEach(cell => {
	  cell.getElement().setAttribute(attrName, attrValue);
	});*/
}

function setFocusField(x) {
	$('#focusField').val(x);
}

function checkMandatory() {
	console.log('checkMandatory calling...');
	for (var i = 0; i < $('.must').length; i++) {
		if ($('.must').eq(i).val() == '') {
			var label = $('.must').eq(i).attr('data-lbl');
			$('.must').eq(i).focus();
			setFocusField('norm!!' + i);
			message('CUW', label + ' is mandatory', 0);
			return false;
		}
	}

	for (var i = 0; i < $('.mandatory').length; i++) {
		var field = $('.mandatory').eq(i).parents('.tabulator-col').attr('tabulator-field');
		var tabId = $('.mandatory').eq(i).parents('.tabulator').attr('id');
		var label = $('.mandatory').eq(i).attr('data-lbl');
		var tbl = getTabulatorVariableNameById(tabId);
		var hdr = $('.' + tabId + '-hdr').text();
		const rows = tbl.getRows();
		for (let row of rows) {
			const data = row.getData();
			if (data.upd == 'Y') {
				if (getCellVal(tbl, data.id, field) == '') {
					setFocusField('tab!!' + tabId + '$' + data.id + '$' + field);
					focusField(tbl, data.id, field);
					if (hdr != '') {
						message('CUW', label + ' is mandatory in ' + hdr, 0);
					} else {
						message('CUW', label + ' is mandatory', 0);
					}
					return false;
				}
			}
		}
	}
	return true;
}

function getTabulatorVariableNameById(containerId) {
	if (tabulatorRegistry[containerId]) {
		return tabulatorRegistry[containerId].instance;
	}
	return null; // Not found
}

function autocomplete(myinput, arr, desc, val) {

	var inp = document.getElementById(myinput);

	$('#' + myinput).parent().after('<input type="hidden" id="' + myinput + '_descHider">');
	$('#' + myinput).parent().after('<input type="hidden" id="' + myinput + '_valHider">');
	/*the autocomplete function takes two arguments,
	the text field element and an array of possible autocompleted values:*/

	var values = [];
	var descs = [];
	for (i = 0; i < arr.length; i++) {
		values.push(arr[i][val]);
		descs.push(arr[i][desc]);
	}
	$('#' + myinput + '_valHider').val(values.join('--'));
	$('#' + myinput + '_descHider').val(descs.join('--'));
	//alert(arr.length);
	//alert(arr[0]['fruit']);

	var currentFocus;
	var ignoreBlur = false; // Flag to ignore blur when clicking an item

	// Function to display all items
	function showAllItems() {
		closeAllLists();
		currentFocus = -1;
		var a = document.createElement("DIV");
		a.setAttribute("id", inp.id + "autocomplete-list");
		a.setAttribute("class", "autocomplete-items");
		inp.parentNode.appendChild(a);

		for (var i = 0; i < arr.length; i++) {
			var b = document.createElement("DIV");
			b.innerHTML = arr[i][desc].substr(0, 0);
			b.innerHTML += arr[i][desc].substr(0);
			b.innerHTML += "<input type='hidden' value='" + arr[i][desc] + "'>";
			b.addEventListener("mousedown", function(e) {
				// Prevent blur from firing when clicking an item
				ignoreBlur = true;
				inp.value = this.getElementsByTagName("input")[0].value;
				closeAllLists();
				ignoreBlur = false;
			});
			a.appendChild(b);
		}
	}

	/*execute a function when someone writes in the text field:*/
	inp.addEventListener("input", function(e) {
		var a, b, i, val = this.value;
		/*close any already open lists of autocompleted values*/
		closeAllLists();

		if (!val) {
			showAllItems(); // Show all when empty
			return false;
		}

		currentFocus = -1;
		/*create a DIV element that will contain the items (values):*/
		a = document.createElement("DIV");
		a.setAttribute("id", this.id + "autocomplete-list");
		a.setAttribute("class", "autocomplete-items");
		/*append the DIV element as a child of the autocomplete container:*/
		this.parentNode.appendChild(a);
		//alert(arr.length);
		/*for each item in the array...*/
		var values = [];
		for (i = 0; i < arr.length; i++) {
			/*check if the item starts with the same letters as the text field value:*/
			if (arr[i][desc].toUpperCase().includes(val.toUpperCase())) {
				/*create a DIV element for each matching element:*/
				b = document.createElement("DIV");
				/*make the matching letters bold:*/
				b.innerHTML = "<strong>" + arr[i][desc] + "</strong>";
				//b.innerHTML += arr[i].substr(val.length);
				/*insert a input field that will hold the current array item's value:*/
				b.innerHTML += "<input type='hidden' value='" + arr[i][desc] + "'>";
				/*execute a function when someone clicks on the item value (DIV element):*/
				b.addEventListener("mousedown", function(e) {
					/*insert the value for the autocomplete text field:*/
					inp.value = this.getElementsByTagName("input")[0].value;
					/*close the list of autocompleted values,
					(or any other open lists of autocompleted values:*/
					closeAllLists();
				});
				b.addEventListener("click", function(e) {
					/*insert the value for the autocomplete text field:*/
					inp.value = this.getElementsByTagName("input")[0].value;
					/*close the list of autocompleted values,
					(or any other open lists of autocompleted values:*/
					closeAllLists();
				});
				a.appendChild(b);
			}
		}
	});

	// Show all items when focused
	inp.addEventListener("focus", function() {
		showAllItems();
	});

	// Hide autocomplete items when input loses focus
	inp.addEventListener("blur", function() {
		if (!ignoreBlur) {
			closeAllLists();
		}
	});

	/*execute a function presses a key on the keyboard:*/
	inp.addEventListener("keydown", function(e) {
		var x = document.getElementById(this.id + "autocomplete-list");
		if (x) x = x.getElementsByTagName("div");
		if (e.keyCode == 40) {
			/*If the arrow DOWN key is pressed,
			increase the currentFocus variable:*/
			currentFocus++;
			/*and and make the current item more visible:*/
			addActive(x);
		} else if (e.keyCode == 38) { //up
			/*If the arrow UP key is pressed,
			decrease the currentFocus variable:*/
			currentFocus--;
			/*and and make the current item more visible:*/
			addActive(x);
		} else if (e.keyCode == 13) {
			/*If the ENTER key is pressed, prevent the form from being submitted,*/
			e.preventDefault();
			if (currentFocus > -1) {
				/*and simulate a click on the "active" item:*/
				if (x) x[currentFocus].click();
			}
		}
	});

	function addActive(x) {
		/*a function to classify an item as "active":*/
		if (!x) return false;
		/*start by removing the "active" class on all items:*/
		removeActive(x);
		if (currentFocus >= x.length) currentFocus = 0;
		if (currentFocus < 0) currentFocus = (x.length - 1);
		/*add class "autocomplete-active":*/
		x[currentFocus].classList.add("autocomplete-active");
	}
	function removeActive(x) {
		/*a function to remove the "active" class from all autocomplete items:*/
		for (var i = 0; i < x.length; i++) {
			x[i].classList.remove("autocomplete-active");
		}
	}
	function closeAllLists(elmnt) {
		/*close all autocomplete lists in the document,
		except the one passed as an argument:*/
		var x = document.getElementsByClassName("autocomplete-items");
		for (var i = 0; i < x.length; i++) {
			if (elmnt != x[i] && elmnt != inp) {
				x[i].parentNode.removeChild(x[i]);
			}
		}
		$('#info_upd').val('Y');
	}
	/*execute a function when someone clicks in the document:*/
	/*document.addEventListener("click", function(e) {
		closeAllLists(e.target);

		var myval = $('#' + myinput).val();
		var my_values = $('#' + myinput + '_valHider').val();
		my_values = my_values.split('--');
		for (var x = 0; x < arr.length; x++) {
			if (myval.trim() == arr[x][desc].trim()) {
				//ChangePlace123
				//alert(my_values[x] + ' for ' + arr[x][desc].trim());
			}
		}

	});*/

	/*document.addEventListener("click", function(e) {
		closeAllLists(e.target);
	});*/

}

function setDrpFld(myinput, val) {
	var my_values = $('#' + myinput + '_valHider').val();
	var my_descs = $('#' + myinput + '_descHider').val();
	my_values = my_values.split('--');
	my_descs = my_descs.split('--');
	var rtn = "";
	for (var x = 0; x < my_values.length; x++) {
		if (my_values[x].toLowerCase().trim() == val.toLowerCase().trim()) {
			rtn = my_descs[x];
		}
	}
	$('#' + myinput).val(rtn);
}

function getDrpFld(myinput) {
	var myval = $('#' + myinput).val();
	var my_values = $('#' + myinput + '_valHider').val();
	var my_descs = $('#' + myinput + '_descHider').val();
	my_values = my_values.split('--');
	my_descs = my_descs.split('--');
	var rtn = "";
	for (var x = 0; x < my_values.length; x++) {
		if (my_descs[x].trim().toLowerCase() == myval.trim().toLowerCase()) {
			rtn = my_values[x];
		}
	}
	return rtn;
}

function isValidEmail(email) {
	// RFC 5322 simplified email regex
	const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
	return emailRegex.test(email);
}

var initImage = '';

//loader(0);

$(function() {
	
	initImage = $('.imageContainerPhoto').attr('src');

	$(document.body).on('input', '.num', function() {
		const sanitized = $(this).val().replace(/\D+/g, '');
		if ($(this).val() !== sanitized) {
			$(this).val(sanitized);
		}
	});

	$('#ok_btn').on('click', function() {
		var focusFld = $('#focusField').val();
		//alert(focusFld);
		if (focusFld != undefined && focusFld.trim() != '') {
			var typ = focusFld.split('!!')[0];
			var idx = focusFld.split('!!')[1];
			if (typ == 'id') {
				setTimeout(function() {
					$('#' + idx).focus();
				}, 500);
			} else if (typ == 'cls') {
				setTimeout(function() {
					$('.' + idx).focus();
				}, 500);
			} else if (typ == 'norm') {
				idx = parseInt(idx);
				setTimeout(function() {
					$('.must').eq(idx).focus();
				}, 500);

			} else if (typ == 'tab') {
				setTimeout(function() {
					var tabId = idx.split('$')[0];
					var ide = idx.split('$')[1];
					var field = idx.split('$')[2];
					//alert(tabId + ' and ' + ide + ' and ' + field);
					var tbl = getTabulatorVariableNameById(tabId);
					focusField(tbl, ide, field);
				}, 1000);
			}

			$('#focusField').val('');
		}
	});

	function focusFieldxy(tbl, rowId, field) {
		var row = tbl.getRow(rowId); // Get the row by ID
		if (row) {
			var cell = row.getCell(field); // Get the cell from the row
			if (cell) {
				cell.edit(); // Open the editor for the cell
			} else {
				console.error("Cell not found for field:", field);
			}
		} else {
			console.error("Row not found for rowId:", rowId);
		}
	}

	$(document.body).on('focus', '.tab_fld', function() {
		$(this).parent().addClass('layer_focus');
		var fld = $(this).parents('.tabulator-cell').attr('tabulator-field');
		var tab_id = $(this).parents('.tabulator').attr('id');
		var tbl = getTabulatorVariableNameById(tab_id);
		var cls = getHeaderAttrByTbl(tbl, fld, 'data-cls');
		var len = getHeaderAttrByTbl(tbl, fld, 'data-len');
		//console.log(cls + ' and ' + len);

		if (cls != null) {
			$(this).addClass(cls);
		}

		if (len != null) {
			$(this).attr('maxlength', len);
		}
	});

	$(document.body).on('blur', '.tab_fld', function() {
		$(this).parent().removeClass('layer_focus');
	});

	storeData('userid', $('#user_id').val());

	// On keydown, prevent entry of non-digit keys except control keys
	$(document.body).on('keydown', '.num', function(e) {
		// Allow control keys: backspace, delete, tab, arrows, home/end, ctrl/cmd combos
		if (
			e.ctrlKey || e.metaKey || e.altKey ||
			$.inArray(e.key, ['Backspace', 'Delete', 'Tab', 'ArrowLeft', 'ArrowRight', 'Home', 'End']) !== -1
		) {
			return;
		}
		// Prevent input if not a digit 0-9
		if (!/^[0-9]$/.test(e.key)) {
			e.preventDefault();
		}
	});

	$(document.body).on('change', '.mail', function() {
		if (!isValidEmail($(this).val())) {
			message('CUW', 'Invalid Email Format', 0);
			$(this).val('');
		}
	});

	$(document.body).on('change', '.money', function() {
		var money = $(this).val().split("$").join("").split(",").join("");
		if (money != '') {
			$(this).val(parseFloat(money).toLocaleString("en-US", { style: "currency", currency: "USD" }));
		}
	});

	function allowNumbers(event) {
		// Allow only backspace, delete and dot
		if (event.keyCode == 46 ||
			event.keyCode == 8 ||
			event.keyCode == 9 ||
			event.keyCode == 110 ||
			event.keyCode == 190 ||
			event.keyCode == 188 ||
			event.keyCode == 189 ||
			event.keyCode == 109 ||
			event.keyCode == 37 ||
			event.keyCode == 39) {
			// let it happen, don't do anything
		} else {
			if (event.keyCode > 47 && event.keyCode < 58) {
				//console.log("Numbers");
			} else if (event.keyCode > 95 && event.keyCode < 106) {
				//console.log("Numpad Numbers");
			} else {
				event.preventDefault();
			}
		}
	}

	$(document.body).on('keydown', '.money', function(event) {
		allowNumbers(event);
	});

	//var current_url = $('#current_url').text();

	//current_url = current_url.split("/");
	//current_url = current_url[current_url.length - 1];

	//getScreenNo(current_url);

	/*function getScreenNo(current_url) {
		$.ajax({
			url: contextPath + 'getScreenNo',
			method: 'POST',
			dataType: "json",
			data: {url: current_url},
			success: function (response) {
				alert(response[0]['SCREEN_CODE']);
				$('#screenCode').val(response[0]['SCREEN_CODE']);
				var srcn_code = response[0]['SCREEN_CODE'];
				getAttrModify(srcn_code);
				getValid(srcn_code);
			}
		});
	}*/

	var srcn_code = $('#screenCode').val();
	var currUser = localStorage.getItem('userId');

	if (currUser != undefined && currUser != '') {
		getUserAccess(currUser);
	}

	if (srcn_code != undefined && srcn_code.trim() != '') {
		//loader(1);
		setTimeout(function() {
			getScreenName(srcn_code);
		}, 1000);
	} else {
		loader(0);
	}

	$('.dtfld').datepicker({
		dateFormat: "mm/dd/yy", // Set the date format
		changeMonth: true,
		changeYear: true,
		yearRange: "1900:2050",
		showButtonPanel: true,  // Show the button panel
		/*onSelect: function() {
			$(this).datepicker("hide"); // Close the datepicker
		}*/
		beforeShow: function(input, inst) {
			setTimeout(() => {
				const buttonPane = $(inst.dpDiv).find(".ui-datepicker-buttonpane");
				if (buttonPane.find(".ui-datepicker-clear").length === 0) {
					$("<button>", {
						text: "Clear",
						class: "ui-datepicker-clear ui-state-default ui-priority-primary ui-corner-all",
						click: function() {
							$.datepicker._clearDate(input);
						}
					}).appendTo(buttonPane);
				}
			}, 1);
		}
	});

	var no = 4;
	var take = 'take : ' + no;
	//alert(take);

	function getValid(srcn_code) {
		var scrncode = srcn_code;

		console.log('in getValid scrncode is ' + scrncode);

		$.ajax({
			url: contextPath + 'getValid',
			method: 'POST',
			dataType: "json",
			data: { screenCode: scrncode },
			success: function(response) {

				if (response.length == 0) {
					getLOV(scrncode);
					return;
				}

				console.log('asif try 1');
				console.log('response.length : ' + response.length);

				for (var i = 0; i < response.length; i++) {
					console.log(response[i]['VALIDATION_LOGIC'] + " for " + response[i]['FIELD_NAME']);

					var typ = response[i]['VALIDATION_LOGIC'].split('::')[0];
					var rul = response[i]['VALIDATION_LOGIC'].split('::')[1];
					var len = response[i]['VALIDATION_LOGIC'].split('::')[2];

					console.log('here typ : ' + typ);
					console.log('here rul : ' + rul);
					console.log('here len : ' + len);

					if (typ == 'text') {
						var field = response[i]['FIELD_NAME'].split('--')[0];
						var tagType = $('.rawField[data-fld="' + field + '"]').prop('tagName').toLowerCase();

						if (tagType == 'div') {

							var col_name = $('.rawField[data-fld="' + field + '"]').parents('.tabulator-col').attr('tabulator-field');
							var tbl_id = $('.rawField[data-fld="' + field + '"]').parents('.tabulator').attr('id');
							var tbl = getTabulatorVariableNameById(tbl_id);

							if (len != 'X' && len > 0) {
								$('.rawField[data-fld="' + field + '"]').attr('data-len', len);
								//$('.' + response[i]['FIELD_NAME']).find('.tab_fld').prop('maxlength', len);
								//setInputLength(tbl, col_name, len);
							}

							if (rul == 'Number') {
								//$('.rawField[data-fld="' + field + '"]').find('.tab_fld').addClass('num');
								$('.rawField[data-fld="' + field + '"]').attr('data-cls', 'num');
							}

							if (rul == 'Email') {
								alert(tbl_id + ' and ' + col_name);

								$('.rawField[data-fld="' + field + '"]').attr('data-cls', 'mail');

								//$('.rawField[data-fld="' + field + '"]').children('.tab_fld').addClass('mail');
								//$('.tabulator-cell[tabulator-field="' + col_name + '"]').addClass('mail');
								//$('#' + tbl_id).find(".tabulator-cell[data-field='" + col_name + "'] input.tab_fld").addClass("mail");
								//$('.tab_fld').addClass('mail');
								//$(tbl.element).find(".tabulator-cell[data-tabulator='typeOfAnimal'] input.tab_fld").addClass("mail");
								//$(tbl.element).find('.tabulator-cell').addClass("mail");

								//addClassToColumnInputs(tbl, col_name, 'mail');
								//alert($(tbl.element).find('.mymail').length);
								//alert(checkMailInputs(tbl));
								//$('.mymail').addClass('test123');

							}

							if (rul == 'Money') {
								//$('.rawField[data-fld="' + field + '"]').find('.tab_fld').addClass('money');
								$('.rawField[data-fld="' + field + '"]').attr('data-cls', 'money');
							}

						} else {

							if (len != 'X' && len > 0) {
								//$('.' + response[i]['FIELD_NAME']).prop('maxlength', len);
								$('.rawField[data-fld="' + field + '"]').prop('maxlength', len);
							}

							if (rul == 'Number') {
								//$('.' + response[i]['FIELD_NAME']).addClass('num');
								$('.rawField[data-fld="' + field + '"]').addClass('num');
							}

							if (rul == 'Email') {
								//$('.' + response[i]['FIELD_NAME']).addClass('mail');
								$('.rawField[data-fld="' + field + '"]').addClass('mail');
							}

							if (rul == 'Money') {
								//$('.' + response[i]['FIELD_NAME']).addClass('mail');
								$('.rawField[data-fld="' + field + '"]').addClass('money');
							}

						}

					} else if (typ == 'date') {
						var min = '';
						var max = '';
						var ddates = [];

						if (rul != 'x') {
							rules = rul.split(",");

							for (var j = 0; j < rules.length; j++) {
								var rule = rules[j].split(":")[0];
								var value = rules[j].split(":")[1].split('|||')[0];
								switch (rule) {
									case 'mindt':
										min = chkToday(value);
										break;
									case 'maxdt':
										max = chkToday(value);
										break;
									case 'mindt+':
										min = differDay(chkToday(value), 'N');
										break;
									case 'maxdt-':
										max = differDay(chkToday(value), 'P');
										break;
									case 'not':
										ddates.push(chkToday(value));
										break;
								}

							}

							//alert(response[i]['FIELD_NAME']);
							var valid_info = min + '::' + max + '::' + ddates.join(',');

							var field = response[i]['FIELD_NAME'].split('--')[0];
							var tagType = $('.rawField[data-fld="' + field + '"]').prop('tagName').toLowerCase();

							console.log('here ' + tagType + ' for ' + field);

							//var ide = $('.rawField[data-fld="' + field + '"]').attr('id');

							//$('#' + ide).attr('data-valid', valid_info);
							//$('#' + ide).attr('data-check', rul);

							if (tagType == 'div') {

								//alert(field);

								var col_name = $('.rawField[data-fld="' + field + '"]').parents('.tabulator-col').attr('tabulator-field');
								var tbl_id = $('.rawField[data-fld="' + field + '"]').parents('.tabulator').attr('id');
								var tbl = getTabulatorVariableNameById(tbl_id);
								//setEditorInputAttributes(tbl, col_name, valid_info, rul);

								/*setTimeout(function() {
									setAttributeForTabulatorCells(tbl, col_name, valid_info, rul);
								},1000);*/

								$('.rawField[data-fld="' + field + '"]').attr('data-valid', valid_info);
								$('.rawField[data-fld="' + field + '"]').attr('data-check', rul);

							} else {
								var ide = $('.rawField[data-fld="' + field + '"]').attr('id');

								$('#' + ide).addClass('dtfld');
								$('#' + ide).attr('data-valid', valid_info);
								$('#' + ide).attr('data-check', rul);
							}

							//$('#' + response[i]['FIELD_NAME']).attr('data-valid', valid_info);
							//$('#' + response[i]['FIELD_NAME']).attr('data-check', rul);

						}
						//applyRange(min, max, ddates, field);
					}



					/*
					if (cat == 'hardcoded' && vals != '') {
						vals = vals.split(',');
						var content = '<option value="">Select ' + response[i]['FIELD_LABEL'] + '</option>';
						for (var j = 0; j < vals.length; j++) {
							content += '<option value="' + vals[j].split(':')[0] + '">' + vals[j].split(':')[1] + '</option>';
						}
	
						$('.' + response[i]['FIELD_NAME']).html(content);
					}
	
					if (cat == 'query' && vals != '') {
						setOption(vals, response[i]['FIELD_NAME'], response[i]['FIELD_LABEL']);
					}
					*/

				}

				getLOV(scrncode);
			},
			error: function(error) {
				//console.error("Error getting table names:", error);
			}
		});
	}

	function getUserAccess(currUser) {

		console.log('in getUserAccess currUser is ' + currUser);

		var accessCtrl = [];

		$.ajax({
			url: contextPath + 'getUserAccess',
			method: 'POST',
			dataType: "json",
			data: { userid: currUser },
			success: function(response) {
				var saveAccess = false;
				var getAccess = false;
				var screen_codex = "";
				if ($('#screenCode').val() != undefined && $('#screenCode').val().trim() != '') {
					screen_codex = $('#screenCode').val();
				}
				for (var i = 0; i < response.length; i++) {
					accessCtrl.push(response[i]['SCREEN_CODE']);
					if (screen_codex != '') {
						if (response[i]['SCREEN_CODE'] == screen_codex) {
							getAccess = true;
							if (response[i]['INSERT_PRIV_FLAG'] == 'Y') {
								saveAccess = true;
							}
						}
					}
				}

				if (screen_codex != '' && !saveAccess) {
					$('#add').remove();
					$('#save').remove();
					$('#saveData').remove();
					$('.updBtn').remove();
				}

				for (var j = 0; j < $('.sidebar-page').length; j++) {
					var dataCode = $('.sidebar-page').eq(j).attr('data-code');
					if (!accessCtrl.includes(dataCode)) {
						$('.sidebar-page').eq(j).css('pointer-events', 'none');
						$('.sidebar-page').eq(j).children().css('color', '#aaa');
						$('.sidebar-page').eq(j).children().children('i').css('color', '#aaa');
						$('.sidebar-page').eq(j).children('a').attr('href', '#');
					}
				}

				if (screen_codex != '' && !getAccess) {
					$('.subheader .btn').not('.header-btn').remove();
					$('#js-page-content').children().not('.subheader').remove();

					var scrnName = $('.subheader-title').text().trim();
					//alert(scrnName);
					$('#js-page-content').append('<h1 style="text-align:center;margin-top:15%;font-size:30px;"><span>You do not have access to this <b>' + scrnName + '</b> screen. <br>Please contact the <b>OMNET</b> administrator.</h1>');
					loader(0);
					return;
				}
			},
			error: function(error) {
				//console.error("Error getting table names:", error);
			}
		});
	}

	function getScreenName(srcn_code) {
		var scrncode = srcn_code;

		console.log('in getScreenName scrncode is ' + scrncode);

		$.ajax({
			url: contextPath + 'getScreenName',
			method: 'POST',
			dataType: "json",
			data: { screenCode: scrncode },
			success: function(response) {

				if (response.length == 0) {
					getAttrModify(srcn_code);
					return;
				}

				console.log('asif try 1');
				console.log('response.length : ' + response.length);

				$('#screenName').text(response[0]['SCREEN_NAME']);

				getAttrModify(srcn_code);
			},
			error: function(error) {
				//console.error("Error getting table names:", error);
			}
		});
	}

	/*var availableTags = [
"ActionScript",
"AppleScript",
"Asp",
"BASIC",
"C",
"C++",
"Clojure",
"COBOL",
"ColdFusion",
"Erlang",
"Fortran",
"Groovy",
"Haskell",
"Java",
"JavaScript",
"Lisp",
"Perl",
"PHP",
"Python",
"Ruby",
"Scala",
"Scheme"
];
 
$( "#tags" ).autocomplete({
source: availableTags
});*/

	function getAttrModify(srcn_code) {
		var scrncode = srcn_code;
		//alert(scrncode);
		$.ajax({
			type: "POST",
			url: contextPath + "getAttr",
			dataType: "json",
			data: { screenCode: scrncode },
			success: function(response) {

				if (response.length == 0) {
					getValid(srcn_code);
					return;
				}

				var mandatory_fld = '';
				for (var i = 0; i < response.length; i++) {

					var field = response[i]['FIELD_NAME'].split('--')[0];
					var ide = '';

					console.log('apple: ' + field + ' and ' + ide);

					console.log('ball: ' + $('.rawField[data-fld="' + field + '"]').prop('tagName'));

					var tagType = $('.rawField[data-fld="' + field + '"]').prop('tagName').toLowerCase();

					console.log('cat: ' + tagType);

					//var ide = $("[name='" + response[i]['FIELD_NAME'] + "']").attr("id");

					/*if (response[i]['READ_ONLY_FLAG'] == 'Y') {
						$("." + response[i]['FIELD_NAME']).attr('readonly', true);
						$("." + response[i]['FIELD_NAME']).css('pointer-events', 'none');
					}*/

					//$('.rawField[data-fld="' + field + '"]').html(response[i]['FIELD_LABEL']);

					if (response[i]['MANDATORY_FLAG'] == 'Y') {

						if (tagType == 'div') {
							//alert('field : ' + field);
							$('.rawField[data-fld="' + field + '"]').html(response[i]['FIELD_LABEL'] + "<span class='text-danger'>*</span>");
							$('.rawField[data-fld="' + field + '"]').addClass('mandatory');
						}
						/*else if(tagType == 'select') {
							$('label[for="' + ide + '"]').html(response[i]['FIELD_LABEL'] + "<span class='text-danger'>*</span>");
							$('select[data-field="' + field + '"] option').first().html(response[i]['FIELD_LABEL'] + "<span class='text-danger'>*</span>");	
						}*/
						else {
							ide = $('.rawField[data-fld="' + field + '"]').attr('id');
							$('label[for="' + ide + '"]').html(response[i]['FIELD_LABEL'] + "<span class='text-danger'>*</span>");
							$('.rawField[data-fld="' + field + '"]').addClass('must');
						}



						/*if ($('.rawField').eq(i).hasClass('tabulator-col-title')) {

							alert(response[i]['FIELD_NAME']);

							$('.rawField[data-fld="' + cls + '"]').eq(i).html(response[i]['FIELD_LABEL'] + "<span class='text-danger'>*</span>");
						}*/

						//alert(response[i]['FIELD_NAME']);
						/*var tab_fld = $('.' + response[i]['FIELD_NAME']).attr('tabulator-field');
						mandatory_fld += "[" + tab_fld + "]";
						var cls = response[i]['FIELD_NAME'];
						$('.' + cls).addClass('mandatory');
						var lbl = $('.rawField[specific="' + cls + '"]').text();
						//alert(lbl);
						$('.rawField[specific="' + cls + '"]').html(lbl + "<span class='text-danger'>*</span>");*/
						//alert(tab_fld);

						//var txt = $("#lbl-" + response[i]['FIELD_NAME']).text();
						//$("#lbl-" + response[i]['FIELD_NAME']).html(txt + "<span class='text-danger'>*</span>");

						/*var txt = $(".permissible[specific=" + response[i]['FIELD_NAME'] + "]").text();
						$(".permissible[specific=" + response[i]['FIELD_NAME'] + "]").html(txt + "<span class='text-danger'>*</span>");
						$("." + response[i]['FIELD_NAME']).addClass('mandatory');*/
					} else {

						if (tagType == 'div' || tagType == 'button') {
							//alert('field : ' + field);
							$('.rawField[data-fld="' + field + '"]').html(response[i]['FIELD_LABEL']);
						} else {
							ide = $('.rawField[data-fld="' + field + '"]').attr('id');
							$('label[for="' + ide + '"]').html(response[i]['FIELD_LABEL']);
						}

					}

					if (response[i]['READ_ONLY_FLAG'] == 'Y') {

						if (tagType == 'div') {
							var col_name = $('.rawField[data-fld="' + field + '"]').parents('.tabulator-col').attr('tabulator-field');
							var tbl_id = $('.rawField[data-fld="' + field + '"]').parents('.tabulator').attr('id');
							var tbl = getTabulatorVariableNameById(tbl_id);
							//setFieldDisabled(tbl, col_name);
							removeEditorFromField(tbl, col_name);
						} else {
							$('.rawField[data-fld="' + field + '"]').attr('readonly', true);
							$('.rawField[data-fld="' + field + '"]').attr('disabled', true);
							$('.rawField[data-fld="' + field + '"]').css('pointer-events', 'none');
						}
					}

					/*if (response[i]['MANDATORY_FLAG'] == 'Y') {
						$('label[for="' + ide + '"]').html(response[i]['FIELD_LABEL'] + "<span style='color:#f00;'>*</span>");
						$('#' + ide).addClass('mandatory');
					} else {
						$('label[for="' + ide + '"]').html(response[i]['FIELD_LABEL']);
						$('#' + ide).removeClass('mandatory');
					}*/


				}

				$('#mandatory_fld').val(mandatory_fld);

				getValid(srcn_code);

			},
			error: function() {
				alert("json not found 123");
			}
		});
	}

	function applyRange(min, max, ddates, cls) {

		if (min == '') {
			min = null;
		}

		if (max == '') {
			max = null;
		}

		$("." + cls).datepicker({
			dateFormat: "mm/dd/yy",
			minDate: min,
			maxDate: max,
			beforeShowDay: function(date) {
				var string = jQuery.datepicker.formatDate('mm/dd/yy', date);
				return [ddates.indexOf(string) == -1];
			}
		});

		$("." + cls).attr("autocomplete", "off");

	}

	$(document).on('change', '.hasDatepicker', function() {

		//alert($(this).val());

		var fld = $(this).parent().attr('tabulator-field');
		var curr = $('#current_fld').val();
		//alert(curr + '&&' + fld);
		if ($(this).val() != '') {
			var mydate = $(this).val();
			if (!valDate(mydate)) {
				$(this).val('');
				//alert("Invalid Date 123");
				//Swal.fire('Alert', 'Date is not valid', 'warning');
				message('CUW', 'Date is not valid', 0);
				setTimeout(function() {
					setValueTab(curr, fld, '');
					focusField(table, curr, fld);
				}, 1000);
			} else {
				var newdt = padDigits(mydate.split("/")[0], 2) + "/" + padDigits(mydate.split("/")[1], 2) + "/" + mydate.split("/")[2];
				if (dateCheck(newdt, fld, 0) != '') {
					$(this).val('');
					var msg = dateCheck(newdt, fld, 0);
					//Swal.fire('Alert', msg, 'warning');
					message('CUW', msg, 0);
					setTimeout(function() {
						setValueTab(curr, fld, '');
						focusField(table, curr, fld);
					}, 1000);
					return;
				}

				if (checkAvailable(newdt, fld, 0)) {
					$(this).val(newdt);
				} else {

					$(this).val('');

					var date_check = $('#' + fld).attr('data-check');

					date_rules = date_check.split(',');

					for (var i = 0; i < date_rules.length; i++) {

						if (!date_rules[i].includes('today')) {
							//console.log('here some specific : ' + date_rules[i]);

							if (date_rules[i].includes('not:')) {
								var getdt = date_rules[i].split("not:")[1].split("|||")[0];
								var getmsg = date_rules[i].split("not:")[1].split("|||")[1];
								if (newdt == getdt) {
									if (getmsg != '') {
										//Swal.fire('Alert', getmsg, 'warning');
										message('CUW', getmsg, 0);
										setTimeout(function() {
											setValueTab(curr, fld, '');
											focusField(table, curr, fld);
										}, 1000);
										return;
									}
								}
							}

							if (date_rules[i].includes('maxdt-:')) {
								var getdt = date_rules[i].split("maxdt-:")[1].split("|||")[0];
								var getmsg = date_rules[i].split("maxdt-:")[1].split("|||")[1];

								var d1 = new Date(newdt.split('/')[2], newdt.split('/')[0] - 1, newdt.split('/')[1]);
								var d2 = new Date(getdt.split('/')[2], getdt.split('/')[0] - 1, getdt.split('/')[1]);

								if (newdt == getdt || d1 > d2) {
									if (getmsg != '') {
										//Swal.fire('Alert', getmsg, 'warning');
										message('CUW', getmsg, 0);
										setTimeout(function() {
											setValueTab(curr, fld, '');
											focusField(table, curr, fld);
										}, 1000);
										return;
									}
								}
							}

							if (date_rules[i].includes('mindt+:')) {
								var getdt = date_rules[i].split("mindt+:")[1].split("|||")[0];
								var getmsg = date_rules[i].split("mindt+:")[1].split("|||")[1];

								var d1 = new Date(newdt.split('/')[2], newdt.split('/')[0] - 1, newdt.split('/')[1]);
								var d2 = new Date(getdt.split('/')[2], getdt.split('/')[0] - 1, getdt.split('/')[1]);

								if (newdt == getdt || d1 < d2) {
									if (getmsg != '') {
										//Swal.fire('Alert', getmsg, 'warning');
										message('CUW', getmsg, 0);
										setTimeout(function() {
											setValueTab(curr, fld, '');
											focusField(table, curr, fld);
										}, 1000);
										return;
									}
								}
							}

							if (date_rules[i].includes('maxdt:')) {
								var getdt = date_rules[i].split("maxdt:")[1].split("|||")[0];
								var getmsg = date_rules[i].split("maxdt:")[1].split("|||")[1];

								var d1 = new Date(newdt.split('/')[2], newdt.split('/')[0] - 1, newdt.split('/')[1]);
								var d2 = new Date(getdt.split('/')[2], getdt.split('/')[0] - 1, getdt.split('/')[1]);

								if (d1 > d2) {
									if (getmsg != '') {
										//Swal.fire('Alert', getmsg, 'warning');
										message('CUW', getmsg, 0);
										setTimeout(function() {
											setValueTab(curr, fld, '');
											focusField(table, curr, fld);
										}, 1000);
										return;
									}
								}
							}

							if (date_rules[i].includes('mindt:')) {
								var getdt = date_rules[i].split("mindt:")[1].split("|||")[0];
								var getmsg = date_rules[i].split("mindt:")[1].split("|||")[1];

								console.log('test alpha : ' + newdt);
								console.log('test beta : ' + getdt);

								var d1 = new Date(newdt.split('/')[2], newdt.split('/')[0] - 1, newdt.split('/')[1]);
								var d2 = new Date(getdt.split('/')[2], getdt.split('/')[0] - 1, getdt.split('/')[1]);

								if (d1 < d2) {
									if (getmsg != '') {
										//Swal.fire('Alert', getmsg, 'warning');
										message('CUW', getmsg, 0);
										setTimeout(function() {
											setValueTab(curr, fld, '');
											focusField(table, curr, fld);
										}, 1000);
										return;
									}
								}
							}


						}

					}

					//alert("Disabled Date Not Allowed");
					//Swal.fire('Alert', 'You cannot enter this date ' + newdt, 'warning');
					message('CUW', 'You cannot enter this date ' + newdt, 0);
				}

			}
		}
	});

	function getLOV(srcn_code) {
		var scrncode = srcn_code;
		console.log('scrncode is ' + scrncode);

		$.ajax({
			url: contextPath + 'getLOV',
			method: 'POST',
			dataType: "json",
			data: { screenCode: scrncode },
			success: function(response) {

				if (response.length == 0) {
					fetchBySession();
					return;
				}

				for (var i = 0; i < response.length; i++) {
					//alert(response[i]['ALGORITHM_QUERY'] + " for " + response[i]['FIELD_NAME']);
					console.log(response[i]['ALGORITHM_QUERY'] + " for " + response[i]['FIELD_NAME']);

					var field = response[i]['FIELD_NAME'].split('--')[0];
					var ide = $('.rawField[data-fld="' + field + '"]').attr('id');
					var tagType = $('.rawField[data-fld="' + field + '"]').prop('tagName').toLowerCase();
					
					var col_name = $('.rawField[data-fld="' + field + '"]').parents('.tabulator-col').attr('tabulator-field');
					
					var tbl_id = $('.rawField[data-fld="' + field + '"]').parents('.tabulator').attr('id');
					var tbl = getTabulatorVariableNameById(tbl_id);

					var cat = response[i]['ALGORITHM_QUERY'].split('::')[0];
					var vals = response[i]['ALGORITHM_QUERY'].split('::')[1];

					if (cat == 'hardcoded' && vals != '') {
						vals = vals.split(',');

						var jsonData = [];
						var jsonData2 = [];
						var entry = [];
						var content = '<option value="">' + response[i]['FIELD_LABEL'] + '</option>';
						for (var j = 0; j < vals.length; j++) {

							//alert(vals[j].split(':')[0]);
							//alert(vals[j].split(':')[1]);
							content += '<option value="' + vals[j].split(':')[0] + '">' + vals[j].split(':')[1] + '</option>';
							entry = {
								value: vals[j].split(':')[0],
								label: capitalizeFirst(vals[j].split(':')[1])
							};

							jsonData.push(vals[j].split(':')[1]);
							jsonData2.push(entry);

						}

						if (tagType == 'select') {
							$('.rawField[data-fld="' + field + '"]').html(content);
						}

						if (tagType == 'input') {
							var myide = $('.rawField[data-fld="' + field + '"]').attr('id');
							autocomplete(myide, jsonData2, "label", "value");
						}

						//$('.rawField[data-fld="' + field + '"]').html(content);
						//var col_fld = $('.' + response[i]['FIELD_NAME']).attr('tabulator-field');

						//const statusColumn = tbl.getColumn(col_name);
						//statusColumn.getDefinition().editorParams.values = jsonData; // Update the editorParams directly

						//selectedOption = jsonData;
						// Refresh the column to apply the new options
						//statusColumn.updateEditorParams();

						//$('#entryForm').append('<input type="value" id="vals_' + col_fld + '">');

						//alert(JSON.stringify(jsonData));

						//$('#vals_' + col_fld).val(JSON.stringify(jsonData2));


						/*var content = '<option value="">Select ' + response[i]['FIELD_LABEL'] + '</option>';
						for (var j = 0; j < vals.length; j++) {
							content += '<option value="' + vals[j].split(':')[0] + '">' + vals[j].split(':')[1] + '</option>';
						}
	
						$('.' + response[i]['FIELD_NAME']).html(content);*/
					}

					if (cat == 'query' && vals != '') {
						setOption(vals, response[i]['FIELD_NAME'], response[i]['FIELD_LABEL'], tbl, col_name, tagType);
					}

					if (cat == 'json' && vals != '') {
						setJSON(vals, response[i]['FIELD_NAME'], response[i]['FIELD_LABEL']);
					}

				}

				fetchBySession();

				//if(!sessionFlag) {
				//loader(0);
				//}
			},
			complete: function(response) {
				//$('#loading-wrapperx').hide();	
			},
			error: function(error) {
				//console.error("Error getting table names:", error);
			}
		});
	}

	function fetchBySession() {
		if ($('#sbiNumberInput').length) {
			var sbiNumberInput = $('#sbiNumberInput');
			if (sbiNumberInput.val().trim() !== '') {
				if (initLoad) {
					$('#searchButton').trigger('click');
					initLoad = false;
				}
			} else {
				loader(0);
			}
		} else {
			loader(0);
		}


		//setTimeout(function() {
		/*var sbiNumberInput = $('#sbiNumberInput');
		if (sbiNumberInput.val().trim() !== '') {
			sessionFlag = true;
			$('#searchButton').trigger('click');
		} else {
			loader(0);
		}*/
		//}, 100);
	}

	function setOption(vals, field, label, tbl, col_name, tagType) {
		
		//alert('col_name is ' + col_name + ' tagtype is ' + tagType);
		
		//alert('inside setOption...');
		vals = vals.split("[q]").join("'");
		if (vals.search('{') != -1) {
			var key = vals.split('{')[1];
			key = key.split('}')[0];
			if (key[0] == '$') {
				key = key.split('$')[1];
				var new_key = "'" + retrieveData(key) + "'";
				vals = vals.split("{$" + key + "}").join(new_key);
				//alert(vals);
			} else {
				console.log('parent key concept');

				$('.rawField[data-fld="' + key + '"]').attr('data-child', field.split('--')[0]);
				return;
			}
		}
		
		//alert(vals);
		
		$.ajax({
			url: contextPath + 'getVals',
			method: 'POST',
			dataType: "json",
			data: { vals: vals },
			success: function(response) {
				var content = '<option value="">' + label + '</option>';
				var jsonData = [];
				var jsonData2 = [];
				var entry = [];
				var jsonData3 = [];
				var entry2 = [];
				
				//alert(response.length + ' for ' + col_name);
				
				for (var i = 0; i < response.length; i++) {
					//console.log(i + ' for ' + col_name + ' = ' + response[i]['OPT']);
					if (response[i]['VAL'] != undefined && response[i]['OPT'] != 'DEFAULT') {
						content += '<option value="' + response[i]['VAL'] + '">' + response[i]['OPT'] + '</option>';
						entry = {
							value: response[i]['VAL'].trim(),
							label: response[i]['OPT'].trim()
						};
						
						entry2 = {
							value: response[i]['VAL'].trim(),
							label: capitalizeFirst(response[i]['OPT'].trim())
						};
					} else {
						content += '<option value="' + response[i]['OPT'] + '">' + response[i]['OPT'] + '</option>';
						entry = {
							value: response[i]['OPT'].trim(),
							label: response[i]['OPT'].trim()
						};
						
						entry2 = {
							value: response[i]['OPT'].trim(),
							label: capitalizeFirst(response[i]['OPT'].trim())
						};
					}

					jsonData.push(response[i]['OPT'].trim());
					jsonData2.push(entry);
					jsonData3.push(entry2);

				}

				//var col_fld = $('.' + field).attr('tabulator-field');

				if (tagType == 'div') {

					//alert('here ' + col_name);

					const statusColumn = tbl.getColumn(col_name);
					statusColumn.getDefinition().editorParams.values = jsonData; // Update the editorParams directly
					//selectedOption = jsonData;
					// Refresh the column to apply the new options
					//statusColumn.updateEditorParams();

					//alert(col_name);

					$('#vals_' + col_name).val(JSON.stringify(jsonData2));

					//alert(col_name + ' and ' + $('#vals_' + col_name).val());

					$('#entryForm').append('<input type="value" id="vals_' + col_fld + '">');

					//alert(JSON.stringify(jsonData));

					$('#vals_' + col_fld).val(JSON.stringify(jsonData2));
				} else if (tagType == 'select') {
					//alert(content);
					field = field.split('--')[0];
					$('.rawField[data-fld="' + field + '"]').html(content);
				} else if (tagType == 'input') {
					field = field.split('--')[0];
					var myide = $('.rawField[data-fld="' + field + '"]').attr('id');
					autocomplete(myide, jsonData3, "label", "value");
				}



				/*let arrayName = col_fld + "_Array"; // The name of the array
				window[arrayName] = []; // Create the array
				window[arrayName] = jsonData;*/

			},
			error: function(error) {
				//console.error("Error getting table names:", error);
			}
		});
	}

	$(document.body).on('change', '.rawField', function() {
		if ($(this).attr('data-child') != undefined) {
			var myField = $(this).attr('data-child');
			var scrn = $('#screenCode').val();
			var field_val = $(this).val();

			var tagType = $('.rawField[data-fld="' + myField + '"]').prop('tagName').toLowerCase();
			var col_name = $('.rawField[data-fld="' + myField + '"]').parents('.tabulator-col').attr('tabulator-field');
			var tbl_id = $('.rawField[data-fld="' + myField + '"]').parents('.tabulator').attr('id');
			var tbl = getTabulatorVariableNameById(tbl_id);

			$.ajax({
				url: contextPath + 'getLOVByField',
				method: 'POST',
				dataType: "json",
				data: { screenCode: scrn, field: myField },
				success: function(response) {
					var lbl = response[0]['FIELD_LABEL'];
					var query = response[0]['ALGORITHM_QUERY'];
					var vals = query.split('query::')[1];
					vals = vals.split("[q]").join("'");
					if (vals.search('{') != -1) {
						var key = vals.split('{')[1];
						key = key.split('}')[0];
						vals = vals.split("{" + key + "}").join("'" + field_val + "'");
					}

				}
			});

		}
	});

	function removeEditorFromField(table, fieldName) {
		// Get the column by field name
		var column = table.getColumn(fieldName);

		// Check if the column exists
		if (column) {
			// Get current column definition
			var columnDef = column.getDefinition();

			// Check if the column has the custom checkbox formatter
			var isCustomCheckbox = columnDef.formatter &&
				typeof columnDef.formatter === 'function' &&
				columnDef.formatter.toString().includes("checkbox");

			// Update the column definition
			column.updateDefinition({
				editor: false,
				formatter: isCustomCheckbox ?
					function(cell) {
						// Create a disabled checkbox element
						var checkbox = document.createElement("input");
						checkbox.type = "checkbox";
						checkbox.checked = cell.getValue(); // Set checked state based on cell value
						checkbox.disabled = true; // Disable the checkbox
						return checkbox; // Return the disabled checkbox element
					} :
					null
			});
		} else {
			console.error("Column with field name '" + fieldName + "' does not exist.");
		}
	}


	function setFieldDisabled(table, fieldName) {
		// Get the current column definitions
		var columns = table.getColumnDefinitions();
		// Loop through the columns to find the one with the specified field name
		for (var i = 0; i < columns.length; i++) {
			if (columns[i].field === fieldName) {
				// Set the editable property to false to make the field disabled
				columns[i].editable = false;
				break; // Exit the loop once the field is found
			}
		}
		// Update the table with the new column definitions
		table.setColumns(columns);
	}

});