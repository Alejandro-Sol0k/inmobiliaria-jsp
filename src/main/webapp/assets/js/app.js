(function () {
    function onlyDigits(value) {
        return (value || '').replace(/\D/g, '');
    }

    function formatThousands(value) {
        var digits = onlyDigits(value);
        return digits.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    }

    document.querySelectorAll('[data-money-input]').forEach(function (input) {
        var form = input.form;
        var hidden = form ? form.querySelector('[data-money-value="' + input.dataset.moneyInput + '"]') : null;
        input.value = formatThousands(input.value);
        input.addEventListener('input', function () {
            var digits = onlyDigits(input.value);
            input.value = formatThousands(digits);
            if (hidden) {
                hidden.value = digits;
            }
        });
        if (hidden) {
            hidden.value = onlyDigits(hidden.value || input.value);
        }
        if (form) {
            form.addEventListener('submit', function () {
                if (hidden) {
                    hidden.value = onlyDigits(input.value);
                }
            });
        }
    });
})();
