//
//  macros.h
//



#define LS(string) NSLocalizedString(string, nil)
#define LIBRARY [NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES) lastObject]
#define Global [AllSingle sharedInstance]
#define SData [ServData sharedObject]
#define GData [GlobalData sharedObject]

#define User [UserData sharedObject]
#define kTapScreenNotification @"tappedScreen"

#define appDelegate ((AppDelegate *) [[UIApplication sharedApplication] delegate])
#define call_completion_block(block,value) if(block!=nil) block(value);
#define contains(str1, str2) ([str1 rangeOfString: str2 ].location != NSNotFound)

#define ApplicationDelegate                 ((AppDelegate *)[[UIApplication sharedApplication] delegate])
#define UserDefaults                        [NSUserDefaults standardUserDefaults]
#define uKey(key)                           [NSString stringWithFormat:@"%@_%@",key,User.user_login]

#define SharedApplication                   [UIApplication sharedApplication]
#define Bundle                              [NSBundle mainBundle]
#define MainScreen                          [UIScreen mainScreen]
#define ShowNetworkActivityIndicator()      [UIApplication sharedApplication].networkActivityIndicatorVisible = YES
#define HideNetworkActivityIndicator()      [UIApplication sharedApplication].networkActivityIndicatorVisible = NO
#define NetworkActivityIndicatorVisible(x)  [UIApplication sharedApplication].networkActivityIndicatorVisible = x
#define NavBar                              self.navigationController.navigationBar
#define TabBar                              self.tabBarController.tabBar
#define NavBarHeight                        self.navigationController.navigationBar.bounds.size.height
#define TabBarHeight                        self.tabBarController.tabBar.bounds.size.height
#define ScreenWidth                         [[UIScreen mainScreen] bounds].size.width
#define ScreenHeight                        [[UIScreen mainScreen] bounds].size.height
#define TouchHeightDefault                  44
#define TouchHeightSmall                    32
#define kKeyboardHeight                     216
#define ViewWidth(v)                        v.frame.size.width
#define ViewHeight(v)                       v.frame.size.height
#define ViewBottom(v)                        v.frame.size.height+v.frame.origin.y

#define ViewX(v)                            v.frame.origin.x
#define ViewY(v)                            v.frame.origin.y
#define SelfViewWidth                       self.view.bounds.size.width
#define SelfViewHeight                      self.view.bounds.size.height
#define RectX(f)                            f.origin.x
#define RectY(f)                            f.origin.y
#define RectWidth(f)                        f.size.width
#define RectHeight(f)                       f.size.height
#define RectSetWidth(f, w)                  CGRectMake(RectX(f), RectY(f), w, RectHeight(f))
#define RectSetHeight(f, h)                 CGRectMake(RectX(f), RectY(f), RectWidth(f), h)
#define RectSetX(f, x)                      CGRectMake(x, RectY(f), RectWidth(f), RectHeight(f))
#define RectSetY(f, y)                      CGRectMake(RectX(f), y, RectWidth(f), RectHeight(f))
#define RectSetSize(f, w, h)                CGRectMake(RectX(f), RectY(f), w, h)
#define RectSetOrigin(f, x, y)              CGRectMake(x, y, RectWidth(f), RectHeight(f))
#define DATE_COMPONENTS                     NSYearCalendarUnit|NSMonthCalendarUnit|NSDayCalendarUnit
#define TIME_COMPONENTS                     NSHourCalendarUnit|NSMinuteCalendarUnit|NSSecondCalendarUnit
#define FlushPool(p)                        [p drain]; p = [[NSAutoreleasePool alloc] init]
#define RGB(r, g, b)                        [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1.0]
#define RGBA(r, g, b, a)                    [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:(a)]
#define RGBColor(color, a)                  [UIColor colorWithRed:(float)((color >> 16) & 0xFF)/255.0f green:(float)((color >> 8) & 0xFF)/255.0f blue:(float)(color & 0xFF)/255.0f alpha:a]

#define FastAlert(t,m)                      [[[UIAlertView alloc] initWithTitle:t message:m delegate:self cancelButtonTitle:@"Закрыть" otherButtonTitles: nil] show]



#define outOfBounds(index,array) (index>=array.count||index<0)

#define CASE(str)                       if ([__s__ isEqualToString:(str)])
#define SWITCH(s)                       for (NSString *__s__ = (s); ; )
#define DEFAULT



#define IS_IPAD (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
#define IS_IPHONE (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
#define IS_IPHONE_4_INCH (IS_IPHONE && [[UIScreen mainScreen] bounds].size.height == 568.0f)
#define IS_IPHONE_3_5_INCH (IS_IPHONE && [[UIScreen mainScreen] bounds].size.height == 480.0f)

#define IOS7_and_upper ([[[UIDevice currentDevice] systemVersion] floatValue]>=7.0f)

#define ID_DEVICE [NSString stringWithFormat:@"x%@",[[UIDevice currentDevice] uniqueDeviceIdentifier]]
#define SYSTEM_VERSION [[[UIDevice currentDevice] systemVersion] floatValue]
#define IOS7_AND_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0)



#define IS_RETINA ([[UIScreen mainScreen] respondsToSelector:@selector(scale)] == YES && [[UIScreen mainScreen] scale] == 2.00)

/// custom
#define alertHeight 100
