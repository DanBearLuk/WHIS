![What Have I Seen](https://raw.githubusercontent.com/DanBearLuk/WHIS/master/media/logo(dark)@4x.png#gh-light-mode-only)
![What Have I Seen](https://raw.githubusercontent.com/DanBearLuk/WHIS/master/media/logo(light)@4x.png#gh-dark-mode-only)
<br></br>

# What Have I Seen

[Посилання на серверну частину проекту](https://github.com/DanBearLuk/WHIS_server)

Мобільний застосунок для операційної системи Android, спрямований на надавання користувачу такого функціоналу, що допоможе йому у пошуку аніме чи манґи та зручному відстежуванні прогресу. 
<br></br>
### Які технології та сервіси використовуються?

Для написання клієнтської частини цього проекту було використано: мову програмування Kotlin та набір інструментів Jetpack Compose.

Серверна ж частина використовує MongoDB для зберігання інформації про користувачів та їх прогресу та Node.js із Express для серверу.

Сама ж інформація про аніме та манґу отримується з публічного безкоштовного API AniList, за що я їм Щиро Дякую! 
<br></br>
### Навігація у застосунку

Всього в застосунку є 5 варіацій сторінки. У чотири з них користувач може потрапити, використовуючи нижню навігаційну панель: домашня сторінка із останніми трендами манґи та аніме; сторінка із пошуком; профіль користувачу або авторизація. Остання п'ята сторінка - це інформація про обране аніме чи манґу, а щоб їх обрати, достатньо натиснути на обкладинку чи опис того, що вас зацікавило.

Далі наведено три сторінки, з якими користувач буде працювати більшу частину часу, а також, приклад навігації у застосунку.

![Main Pages](https://raw.githubusercontent.com/DanBearLuk/WHIS/master/media/Pages.png)

https://github.com/DanBearLuk/WHIS/assets/39224159/42c97be7-8c9e-4fc4-8aca-be82a3a67940

### Домашня сторінка

Як вже було зазначено, на цій сторінці можна переглянути останні тренди. Свайпаючи вліво або вправо, або натискаючи на індикатор, що відображається знизу, можна перемикати категорії:
- All - манґа та аніме
- Manga - манґа
- Anime - аніме

https://github.com/DanBearLuk/WHIS/assets/39224159/490cbb54-8bdc-4c65-8d00-b7b580e9d150

### Сторінка з пошуком

На цій сторінці звісно ж присутнє поле пошуку, куди можна вводити назву та отримувати результати пошуку по всім категоріям. Натискаючи на опис чи обкладинку результату, можна переглянути усю інформацію.

https://github.com/DanBearLuk/WHIS/assets/39224159/cb4d354b-9d87-4b03-be14-434e0ca30750

### Авторизація або профіль користувача

Натиснувши на цю сторінку у навігаційній панелі, у вас буде два можливі результати: у першому ви потрапите до сторінки реєстрації та авторизації, у другому до профілю користувача. Жоден з цих варіантів не є поганим, реєстрація нескладна та займає не більше однієї хвилини.

![Profile Screens](https://raw.githubusercontent.com/DanBearLuk/WHIS/master/media/ProfileScreens.png)

Профіль користувача дууууже корисний, в ньому ви можете: вийти з акаунту, видалити збережену манґу чи аніме, оновити прогрес, перемикнути категорію та переглянути усю статистику. 

Найважливіший функціонал: оновлення прогресу. Для оновлення вам потрібно натиснути на коло з іконкою книжки або числами, після чого можна буде редагувати прогрес. Верхнє число показує кількість епізодів чи глав, які ви переглянули, тоді як нижнє число вказує на загальну кількість. Іконка книжки означає, що інформація про загальну кількість відсутня, отже, можна лише відзначити однє з трьох: плануєте дивитись/читати, у процесі перегляду/читання, закінчили.

Для видалення потрібно лише затиснути потрібне аніме чи манґу, та, коли з'явится знак контейнеру для сміття на червоному фоні, натиснути на нього. Якщо ви передумали, то не хвилюйтеся, цей значок зникне через 5 секунд, якщо з ним не взаємодіяти.

https://github.com/DanBearLuk/WHIS/assets/39224159/5896b24c-6822-43a1-95d7-871097555c19

https://github.com/DanBearLuk/WHIS/assets/39224159/dd867cc2-bbe3-4758-8216-7b4c053fdd46

### Інформація про манґу чи аніме

Хоч ця сторінка й остання, але, вона не менш важлива. Ця сторінка містить усю інформацію, що вас може зацікавити стосовно обраної манґи чи аніме. Тут присутній і опис, і дата виходу, і сезон, і поточний стан, навіть список тегів можна переглянути. Для перемикання між потрібною інформацією, можна натиснути на відповідні написи зліва та справа, або, свайпнути у відповідну сторону.

![Title Info](https://raw.githubusercontent.com/DanBearLuk/WHIS/master/media/Title.png)

Крім того, на обкладинці, що зображена зверху, є червона кнопочка зі знаком "+". Натиснувши на неї, ви додасте цю манґу/аніме до вашого списку збереженого.

https://github.com/DanBearLuk/WHIS/assets/39224159/57cce043-888a-4975-a803-8da552e25e09
