   	
		/*floating dropdown common condition Start*/
			
			/* First option in SELECT tag don't need to be BLANK */
		$('.floatingLableClick').on('focus blur change', function (e) {
			var $currEl = $(this);
		  
		  if($currEl.is('select')) {
		  	if($currEl.val() === $("option:first", $currEl).val()) {
		    	$('.selectFloating', $currEl.parent()).animate({opacity: 0}, 240);
		      $currEl.parent().removeClass('focused');
		    } else {
		    	$('.selectFloating', $currEl.parent()).css({opacity: 1});
		    	$currEl.parents('.form-group').toggleClass('focused', ((e.type === 'focus' || this.value.length > 0) && ($currEl.val() !== $("option:first", $currEl).val())));
		    }
		  } else {
		  	$currEl.parents('.form-group').toggleClass('focused', (e.type === 'focus' || this.value.length > 0));
		  }
		}).trigger('blur');
		
		/*floating dropdown common condition End*/
		
		$(':input').on('focus', function () {
		  $(this).attr('autocomplete', 'off')
		});