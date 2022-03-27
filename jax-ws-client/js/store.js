document.addEventListener('DOMContentLoaded', function () {
    // lấy table body để thay đổi
    var tableBody = document.getElementById('my-table-data');
    // xử lý request lên server.
    var xmlHttpRequest = new XMLHttpRequest();
    // sự kiện khi request thay đổi trạng thái
    xmlHttpRequest.onreadystatechange = function () {

        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            var data = JSON.parse(xmlHttpRequest.responseText);
            var newContent = '';
            for (let i = 0; i < data.length; i++) {

                newContent += `
                <tr>
                    <td class="shop-item-id">${data[i].id}</td>
                    <td class="shop-item-title">${data[i].name}</td>
                    <td class="shop-item-price">${data[i].price}</td>
                    
                    <td>
                        <a href="form.html?id=${data[i].id}" class="btn-edit">Edit</a> 
                        <a href="#" title="${data[i].id}" class="btn-delete">Delete</a>
                        
                        <a href="#" title="${data[i].id}" class="shop-item-button">Add to Cart</a>
                    </td>
                </tr>`;
            }
            tableBody.innerHTML = newContent; // thay đổi nội dung table.
        }
    };
    xmlHttpRequest.open('get', 'http://localhost:8088/api/products', false);
    xmlHttpRequest.send();

    // kiểm tra nếu click vào btn delete thì sẽ delete Product đó đi
    document.body.addEventListener('click', function (event) {
        if (event.target.className === 'btn-delete') {
            if (confirm('Are you sure you want to delete?')) {
                let id = event.target.title;
                const xmlHttpRequest = new XMLHttpRequest();
                xmlHttpRequest.onreadystatechange = function () {
                    if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
                        alert('Deleted successfully');
                        location.reload();
                    }
                };
                xmlHttpRequest.open(
                    'delete',
                    'http://localhost:8088/api/products/' + id,
                    false
                );
                xmlHttpRequest.send();
            }
        }
    });
    //-------------------------------------------------------------------------
    //ADD TO CART
    if (document.readyState == 'loading') {
        document.addEventListener('DOMContentLoaded', ready)
    } else {
        ready()
    }

    function ready() {
        var removeCartItemButtons = document.getElementsByClassName('btn-danger')
        for (var i = 0; i < removeCartItemButtons.length; i++) {
            var button = removeCartItemButtons[i]
            button.addEventListener('click', removeCartItem)
        }

        var quantityInputs = document.getElementsByClassName('cart-quantity-input')
        for (var i = 0; i < quantityInputs.length; i++) {
            var input = quantityInputs[i]
            input.addEventListener('change', quantityChanged)
        }

        var addToCartButtons = document.getElementsByClassName('shop-item-button')
        for (var i = 0; i < addToCartButtons.length; i++) {
            var button = addToCartButtons[i]
            button.addEventListener('click', addToCartClicked)
        }

        document.getElementsByClassName('btn-purchase')[0].addEventListener('click', purchaseClicked)
    }

    var btnSubmit = document.getElementById("btn-submit");
    var txtProduct_Id = document.getElementById('product_Id')
    var txtTotalPrice = document.getElementById('totalPrice')
    var txtQuantity = document.getElementById('quantity')
    var url_string = window.location.href.toLowerCase();
    var url = new URL(url_string);
    var id = url.searchParams.get('id');
    if (id != undefined && id.length > 0) {
        isEdit = true;
        let xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.onreadystatechange = function () {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
                var data = JSON.parse(xmlHttpRequest.responseText);
                txtProduct_Id.value = data.name;
                txtTotalPrice.value = data.price;
                txtQuantity.value = data.status;
            }
        }
    };



    function purchaseClicked() {

        var cartItems = document.getElementsByClassName('cart-items')[0]
        while (cartItems.hasChildNodes()) {
            cartItems.removeChild(cartItems.firstChild)
        }


        btnSubmit.onclick = function () {
            var button = event.target
            var shopItem = button.parentElement.parentElement
            var product_Id = shopItem.getElementsByClassName('shop-item-id')[0].innerText
            var quantity = shopItem.getElementsByClassName('cart-quantity')[0].innerText
            var totalPrice = shopItem.getElementsByClassName('shop-item-price')[0].innerText
            // var product_Id = product_Id;
            // var totalPrice = totalPrice;
            // var quantity = quantity;


            var dataToSend = {
                product_Id: product_Id,
                totalPrice: totalPrice,
                quantity: quantity,
            };


            var method = 'post';
            var url = 'http://localhost:8088/api/cart';
            var successStatus = 201;


            var xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onreadystatechange = function () {

                if (
                    xmlHttpRequest.readyState == 4 &&
                    xmlHttpRequest.status == successStatus
                ) {
                    alert('Create cart success!');
                    window.location.href = 'store.html';
                }
            };
            xmlHttpRequest.open(method, url, false);

            xmlHttpRequest.setRequestHeader('Content-Type', 'application/json');
            xmlHttpRequest.send(JSON.stringify(dataToSend));
        };

    }

    function removeCartItem(event) {
        var buttonClicked = event.target
        buttonClicked.parentElement.parentElement.remove()
        updateCartTotal()
    }

    function quantityChanged(event) {
        var input = event.target
        if (isNaN(input.value) || input.value <= 0) {
            input.value = 1
        }
        updateCartTotal()
    }

    function addToCartClicked(event) {
        var button = event.target
        var shopItem = button.parentElement.parentElement
        var item_id = shopItem.getElementsByClassName('shop-item-id')[0].innerText
        var title = shopItem.getElementsByClassName('shop-item-title')[0].innerText
        var price = shopItem.getElementsByClassName('shop-item-price')[0].innerText

        addItemToCart(title, price)
        updateCartTotal()
    }

    function addItemToCart(title, price) {
        var cartRow = document.createElement('div')
        cartRow.classList.add('cart-row')
        var cartItems = document.getElementsByClassName('cart-items')[0]
        var cartItemNames = cartItems.getElementsByClassName('cart-item-title')
        for (var i = 0; i < cartItemNames.length; i++) {
            if (cartItemNames[i].innerText == title) {
                alert('This item is already added to the cart')
                return
            }
        }
        var cartRowContents = `

        <div class="cart-item cart-column">
            
            <span class="cart-item-title">${title}</span>
        </div>
        <span class="cart-price cart-column">${price}</span>
        <div class="cart-quantity cart-column">
            <input class="cart-quantity-input" type="number" value="1">
            <button class="btn btn-danger" type="button">REMOVE</button>
        </div>`
        cartRow.innerHTML = cartRowContents
        cartItems.append(cartRow)
        cartRow.getElementsByClassName('btn-danger')[0].addEventListener('click', removeCartItem)
        cartRow.getElementsByClassName('cart-quantity-input')[0].addEventListener('change', quantityChanged)
    }

    function updateCartTotal() {
        var cartItemContainer = document.getElementsByClassName('cart-items')[0]
        var cartRows = cartItemContainer.getElementsByClassName('cart-row')
        var total = 0
        for (var i = 0; i < cartRows.length; i++) {
            var cartRow = cartRows[i]
            var priceElement = cartRow.getElementsByClassName('cart-price')[0]
            var quantityElement = cartRow.getElementsByClassName('cart-quantity-input')[0]
            var price = parseFloat(priceElement.innerText.replace('$', ''))
            var quantity = quantityElement.value
            total = total + (price * quantity)
        }
        total = Math.round(total * 100) / 100
        document.getElementsByClassName('cart-total-price')[0].innerText = '$' + total
    }
});

