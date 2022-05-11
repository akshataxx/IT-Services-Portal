function addDynamicSelect(main,secondary, collections) {
    let chooseCollections = document.querySelector(`#${main}`);
    let selectItems = document.querySelector(`#${secondary}`);

    chooseCollections.addEventListener('change', (e) => {
        selectItems.querySelectorAll('*').forEach(el => el.remove());
        const target = e.target.value;
        applyOptions(selectItems,target, collections);
    });

    applyOptions(selectItems,chooseCollections.options[chooseCollections.selectedIndex].value,collections);
}

function applyOptions(selectItems,target,collections) {
    const categories = collections[target];
    for(let i=0;i<categories.length;i++) {
        const option = document.createElement('option');
        const value = categories[i];
        option.value = value.value;
        option.innerHTML = value.name;
        selectItems.appendChild(option);
    }
}