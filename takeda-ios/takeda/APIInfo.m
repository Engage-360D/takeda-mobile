
#define kBaseServerURL @"http://cardiomagnyl.dev.iengage.ru"
// #define kBaseServerURL @"http://cardiomagnyl.iengage.ru"
// #define kBaseServerURL @"http://cardiomagnyl.iengage.ru"

#define kReportURL @"http://cardiomagnyl.dev.iengage.ru"


#define kServerURL [NSString stringWithFormat:@"%@/api/v1/",kBaseServerURL]
#define kTokens @"tokens"   // получить токен пользователя (исп. для авторизации)
#define kUsers @"users"
#define kAccount @"account" // Пользователи. POST - создать пользователя, GET - получить пользователей, /id - информация о пользователе.
#define kRegionsList @"regions" // получить список регионов.
#define kTestResults @"account/test-results" // POST - сохранение результатов теста , GET - (получение результата(ов) теста пользователя.
#define kAccountResetPass @"account/reset-password" // Выслать новый пароль на почту пользователю. POST.
#define kTestResultShareEmail @"send-email" // Выслать результаты теста на почту. POST.
#define kAccountPills @"account/pills" // Таблетки. POST - Добавить таблетку. GET - получить таблетки.


#define client_id @"3_4d30arhx9jmssw4owoc8oksw48os0cccssoogs84kc008ogco4"
#define client_secret @"6bhq30w1jrc4wwoc00ks400scgkwws8g8skwkc84g0kkwgkc0k"
