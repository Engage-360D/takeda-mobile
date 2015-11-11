
#define kBaseServerURL @"http://cardiomagnyl.dev.iengage.ru"
// #define kBaseServerURL @"http://cardiomagnyl.iengage.ru"
// #define kBaseServerURL @"http://cardiomagnyl.iengage.ru"

#define kReportURL @"http://cardiomagnyl.dev.iengage.ru/account/reports"


#define kServerURL [NSString stringWithFormat:@"%@/api/v1/",kBaseServerURL]
#define kTokens @"tokens"   // получить токен пользователя (исп. для авторизации)
#define kFacebook @"facebook"
#define kVK @"vk"
#define kOK @"odnoklassniki"
#define kGp @"google"

#define kUsers @"users"
#define kAccount @"account" // Пользователи. POST - создать пользователя, GET - получить пользователей, /id - информация о пользователе.
#define kRegionsList @"regions" // получить список регионов.
#define kTestResults @"account/test-results" // POST - сохранение результатов теста , GET - (получение результата(ов) теста пользователя.
#define kAccountResetPass @"account/reset-password" // Выслать новый пароль на почту пользователю. POST.
#define kTestResultShareEmail @"send-email" // Выслать результаты теста на почту. POST.
#define kAccountPills @"account/pills" // Таблетки. POST - Добавить таблетку. GET - получить таблетки.
#define kAccountTimeline @"account/timeline" // Таймлайн. Он же Дневник. GET.
#define kAccountTasks @"account/timeline/tasks" // Задачи с таймлайна. PUT - обновить, GET - посмотреть по id.
#define kAccountIncidents @"account/incidents" // Добавить инцидент. POST.
#define kTestResultDietQuestions @"diet-questions" // получить вопросы для теста по питанию. GET.
#define kTestResultDietRecommendations @"diet-recommendations" // получить результаты теста по питанию. GET.

#define kInstitutionTowns @"institution-parsed-towns" // Список городов для поиска. GET.
#define kInstitutionSpecializations @"institution-specializations" // Список специальностей для поиска. GET.
#define kInstitutionsList @"institutions" // Список ЛПУ с поиска. GET.
#define kAccountReset @"account/reset" // Ресет пользователя. POST.
#define kAccountISR @"account/isr" // ИСП пользователя. GET.


#define kSpecAutoSearchLPU @"Кардиология"
#define kScoreLink @"http://cvdrisk.nhlbi.nih.gov"
#define kProcamLink @"http://www.chd-taskforce.com/procam_interactive.html"


#pragma mark - App keys 

#define client_id @"3_4d30arhx9jmssw4owoc8oksw48os0cccssoogs84kc008ogco4"
#define client_secret @"6bhq30w1jrc4wwoc00ks400scgkwws8g8skwkc84g0kkwgkc0k"

#pragma mark -


// External defines
// Don't forget make changes in takeda-Info.plist

#pragma mark - OK

#define  ok_appId @"1126090240"
#define  ok_appKey @"CBAHMPBEEBABABABA"
#define  ok_appSecret @"48E60A55CB4CF49C3E5D3762"


#pragma mark - VK

#define vkAppId @"4289667"
#define VK_SCOPE @[VK_PER_FRIENDS, VK_PER_WALL, VK_PER_EMAIL, VK_PER_PHOTOS]


#pragma mark - Google+

#define kGoogleClientIDKey @"83199848119-m2fg4n63sp12m5ipqcv2712ok8erk264.apps.googleusercontent.com"
#define kGoogleClientSecretKey @"y07UIpqqTOPKeho_hJlT2IGP"


#pragma mark - Facebook

#define FB_SCOPE @[@"email", @"read_stream", @"user_about_me", @"user_birthday"]
#define kFacebookApId @"842362822502496"
#pragma mark -


#pragma mark - TestFairy

#define kTestFairyId @"0a5b866d28f84bb9bbea5a2948683e8e0be28546"

#pragma mark -


#pragma mark - GoogleMaps

#define kGoogleMapsId @"AIzaSyCJbaqLyduDBLnzodgcq5WdD7ebS2tU2DM"

#pragma mark -


#pragma mark - GoogleAnalytics

#define kGATrackingId @"UA-67616061-2" // For test - @"UA-43892815-3"

#pragma mark -


#define kReachabilityHost @"www.apple.com"

