document.addEventListener('DOMContentLoaded', function() {

    const button = document.getElementById('submit_btn');
    button.addEventListener('click', function() {

        const cust_name = document.getElementById('cust_name');
        const cust_phone = document.getElementById('cust_phone');
        const cust_greeting = document.getElementById('cust_greeting');
        const url = '/call' + 
            '?name=' + encodeURIComponent(cust_name.value) + 
            '&phoneNumber=' + encodeURIComponent(cust_phone.value) +
            '&greeting=' + encodeURIComponent(cust_greeting.value);
        console.log(url);
        
        // Send the POST request
        fetch(url, {method: 'GET' })
          .then(response => response)
          .catch(error => console.error('Error:', error));
      
    });
});
