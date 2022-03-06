# AC0322
- Basic Spring Boot application for renting a tool
- Checkout info is received via a post request with the method of your choosing, for my testing I ran against localhost and utilized Postman.
- If your checkout info is valid, an HTTP response of OK is returned you are presented with a listing of your rental agreement info:
 ![Screen Shot 2022-03-06 at 3 22 06 PM](https://user-images.githubusercontent.com/72402912/156940757-07225923-db07-4fc7-96e9-a901fcbd62d2.png)

- If your checkout info is invalid, an HTTP response of BadRequest is returned and you are presented with the reason(s) why:
![postman_invalid_screenshot](https://user-images.githubusercontent.com/72402912/156940703-c959f8cc-1126-463e-9b90-38a7ef17a5a7.png)

