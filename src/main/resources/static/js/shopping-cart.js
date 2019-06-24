const CART = {
    KEY: 'cartProducts',
    products: [],
    init() {
        const cartArray = localStorage.getItem(CART.KEY);
        if (cartArray) {
            CART.products = JSON.parse(cartArray);
        } else {
            CART.products = [];
            CART.sync();
        }
    },
    async sync() {
        let cart = JSON.stringify(CART.products);
        await localStorage.setItem(CART.KEY, cart);
    },
};

function find(id) {
    let match = CART.products.filter(prod => {
        if (prod.id === id) {
            return true;
        }
    });
    if (match && match[0]) {
        return match[0];
    }
}
function addToCart(id) {
    let imgUrl = document.getElementById('prodImgUrl'+id).getAttribute('src');
    let name = document.getElementById('prodName'+id).innerText;
    let brand = document.getElementById('prodBrand'+id).innerText;
    let desc = document.getElementById('prodDesc'+id).innerText;
    let price = document.getElementById('prodPrice'+id).innerText;
    if (find(id)) {
        increase(id,1);
    } else {
        let prod = {
            id: id,
            name: name,
            brand: brand,
            description: desc,
            price: price,
            imageUrl: imgUrl,
            quantity: 1
        };
        CART.products.push(prod);
        CART.sync();
    }
}
function increase(id, quantity=1) {
    CART.products = CART.products.map(prod => {
        if (prod.id === id) {
            prod.quantity = prod.quantity + quantity;
        }
        return prod;
    });
    CART.sync();
}
function reduce(id, quantity=1) {
    CART.products = CART.products.map(prod => {
        if (prod.id === id) {
            prod.quantity = prod.quantity - quantity;
        }
        return prod;
    });
    CART.products.forEach(async prod => {
        if (prod.id === id && prod.quantity === 0) {
            await remove(id);
        }
    });
    CART.sync();
}
function remove(id) {
    CART.products = CART.products.filter(item => {
        if (item.id !== id) {
            return true;
        }
    });
    CART.sync();
}

document.addEventListener('DOMContentLoaded', ()=> {
    CART.init();
});