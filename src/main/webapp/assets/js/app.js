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

    document.querySelectorAll('[data-property-select]').forEach(function (select) {
        var preview = document.querySelector('[data-property-preview="' + select.dataset.propertySelect + '"]');
        if (!preview) return;
        var image = preview.querySelector('img');
        var title = preview.querySelector('strong');

        function updatePreview() {
            var option = select.options[select.selectedIndex];
            if (!option || !option.value) {
                preview.classList.add('d-none');
                return;
            }
            image.src = option.dataset.image || '';
            image.alt = option.dataset.title || 'Propiedad seleccionada';
            title.textContent = option.dataset.title || 'Propiedad seleccionada';
            preview.classList.remove('d-none');
        }

        select.addEventListener('change', updatePreview);
        updatePreview();
    });
})();
