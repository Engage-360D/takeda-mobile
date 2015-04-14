//
//  UsefulKnowPage.m
//  takeda
//
//  Created by Alexander Rudenko on 19.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "UsefulKnowPage.h"

@interface UsefulKnowPage (){
    NSMutableDictionary *postForShare;
    UIWebView *webView;
}

@end

@implementation UsefulKnowPage
@synthesize infoData;

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = RGB(236, 236, 236);
    self.tableView.tableHeaderView = self.tableView.topSepar;
    UIImageView *s = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.tableView.width, 0.5)];
    s.backgroundColor = self.tableView.separatorColor;

    [self.danger_text addSubview:s];
    self.tableView.tableFooterView = self.danger_text;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self removeActivityIdicator];
    [self initData];
    
}

-(void)initData{

    NSString *path = [[NSBundle mainBundle] pathForResource:
                      @"UsefulKnowContent" ofType:@"plist"];
    
    infoData = [Global recursiveMutable:[[NSMutableArray alloc] initWithContentsOfFile:path]];
    [self.tableView reloadData];
}


#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return infoData.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    UsefulKnowCell *cell = (UsefulKnowCell*)[self tableView:tableView cellForRowAtIndexPath:indexPath];
    return [cell heightCell:infoData[indexPath.row]];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"UsefulKnowCell";
    
    UsefulKnowCell *cell = (UsefulKnowCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"UsefulKnowCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[UsefulKnowCell class]])
            {
                cell = (UsefulKnowCell *)currentObject;
                for (UIButton *btn in cell.shareBtns){
                    [btn addTarget:self action:@selector(shareAction:) forControlEvents:UIControlEventTouchUpInside];
                }
                
                break;
            }
        }
    }
    
    NSMutableDictionary *menu = infoData[indexPath.row];
    [cell setupCell:menu];
    cell.sharePanel.tag = indexPath.row;
    cell.backgroundColor = RGB(236, 236, 236);
    cell.contentView.backgroundColor = RGB(236, 236, 236);

    return cell;
}

#pragma mark - Table view delegate

-(void)shareAction:(UIButton*)sender{
    [self showActivityIndicatorWithString:@"Публикация записи"];
// http://cardiomagnyl.dev.iengage.ru/good-to-know?blockId=1#1
    int index = sender.superview.tag;
    int social = sender.tag;
    NSMutableDictionary *post = infoData[index];
    [post setObject:[NSNumber numberWithInt:index+1] forKey:@"index"];
    switch (social) {
        case 1:{
            [self shareByVK:post];
            break;
        }
        case 2:{
            [self shareByFB:post];
            break;
        }
        case 3:{
            [self shareByOK:post];
            break;
        }
        case 4:{
            [self shareByGp:post];
            break;
        }
    }
    
}

#pragma mark - Sharing
#pragma mark - VK




-(void)sharePostByVK{
    NSDictionary *params = @{VK_API_ATTACHMENTS : [self urlToShare], VK_API_MESSAGE : [NSString stringWithFormat:@"Полезно знать"]};
    VKRequest *post = [[VKApi wall] post:params];
    [post executeWithResultBlock: ^(VKResponse *response) {
        [self successMessage];
    } errorBlock: ^(NSError *error) {
        NSLog(@"Error: %@", error);
        [self failMessage];
        
    }];

//    
//      with photo
//    
//    NSString *userId = [VKSdk getAccessToken].userId;
//    //предварительная загрузка фото на сервер
//    VKRequest *request = [VKApi uploadWallPhotoRequest:[UIImage imageNamed:postForShare[@"image"]] parameters:[VKImageParameters jpegImageWithQuality:1.f] userId:[userId integerValue] groupId:0];
//    [request executeWithResultBlock: ^(VKResponse *response) {
//        VKPhoto *photoInfo = [(VKPhotoArray*)response.parsedModel objectAtIndex:0];
//        NSString *photoAttachment = [NSString stringWithFormat:@"photo%@_%@", photoInfo.owner_id, photoInfo.id];
//        NSDictionary *params = @{ VK_API_ATTACHMENTS : photoAttachment,
//                                  VK_API_FRIENDS_ONLY : @(0),
//                                  VK_API_OWNER_ID : userId,
//                                  VK_API_MESSAGE : [NSString stringWithFormat:@"%@\n\n%@",postForShare[@"title"],postForShare[@"text"]]
//                                  };
//        VKRequest *post = [[VKApi wall] post:params];
//        [post executeWithResultBlock: ^(VKResponse *response) {
//            [self successMessage];
//        } errorBlock: ^(NSError *error) {
//            NSLog(@"Error: %@", error);
//            [self failMessage];
//
//        }];
//
//    } errorBlock: ^(NSError *error) {
//        NSLog(@"Error: %@", error);
//        [self failMessage];
//    }];
}

- (void)vkSdkNeedCaptchaEnter:(VKError *)captchaError {
    VKCaptchaViewController *vc = [VKCaptchaViewController captchaControllerWithError:captchaError];
    [vc presentIn:self.navigationController.topViewController];
}

- (void)vkSdkTokenHasExpired:(VKAccessToken *)expiredToken {
    [VKSdk authorize:VK_SCOPE revokeAccess:YES];
}

- (void)vkSdkReceivedNewToken:(VKAccessToken *)newToken {
    [self sharePostByVK];
}

- (void)vkSdkShouldPresentViewController:(UIViewController *)controller {
    [self.navigationController.topViewController presentViewController:controller animated:YES completion:nil];
}

- (void)vkSdkAcceptedUserToken:(VKAccessToken *)token {
    [self sharePostByVK];
}
- (void)vkSdkUserDeniedAccess:(VKError *)authorizationError {
    [self failMessage];
}
-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    [self.navigationController popToRootViewControllerAnimated:YES];
}

-(void)giveServerForUpload{
    VKRequest * request = [[VKApi wall] post:@{VK_API_MESSAGE : @"Test"}];
    
    [request executeWithResultBlock:^(VKResponse * response) {
        [self successMessage];
        NSLog(@"Json result: %@", response.json);
        
        
    } errorBlock:^(NSError * error) {
        [self failMessage];
        NSLog(@"VK error: %@", error);
    }];

}




-(void)shareByVK:(NSMutableDictionary*)post{
    
    postForShare = post;
    
        [VKSdk initializeWithDelegate:self andAppId:vkAppId];

    if ([VKSdk wakeUpSession])
        {
            [self sharePostByVK];
        } else {
            [VKSdk authorize:VK_SCOPE revokeAccess:YES];
        }

}

-(void)shareByFB:(NSMutableDictionary*)post{
    postForShare = post;
    if (FBSession.activeSession.isOpen) {
        [self requestFbPublishPermissionsAndPublish];
    } else {
        NSArray *permissions = FB_SCOPE;
        [FBSession openActiveSessionWithReadPermissions:permissions
                                           allowLoginUI:YES
                                      completionHandler:^(FBSession *session,
                                                          FBSessionState status,
                                                          NSError *error) {
                                          // if login fails for any reason, we alert
                                          if (error) {
                                              UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                                              message:error.localizedDescription
                                                                                             delegate:nil
                                                                                    cancelButtonTitle:@"OK"
                                                                                    otherButtonTitles:nil];
                                              [alert show];
                                          } else if (FB_ISSESSIONOPENWITHSTATE(status)) {
                                              [self requestFbPublishPermissionsAndPublish];
                                          }
                                      }];
    }

}

-(void)sharePostByOK{
    NSDictionary *attachments = @{ @"media" : @[@{@"url" : [self urlToShare], @"type" : @"link"},
                                                @{@"text":postForShare[@"title"], @"type":@"text"}]};
    
    
    OKMediaTopicPostViewController *pvc = [OKMediaTopicPostViewController postViewControllerWithAttachments:attachments];
    [pvc presentInViewController:self];
    
    __weak __typeof (self)wSelf = self;
    pvc.resultBlock = ^(BOOL result, BOOL canceled, NSError *error) {
        if (!canceled) {
            [self successMessage];
        } else {
            [self failMessage];
        }
        [wSelf dismissViewControllerAnimated:YES completion:nil];
    };

}

-(void)shareByOK:(NSMutableDictionary*)post{
    postForShare = post;
    
//    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://connect.ok.ru/dk?st.cmd=WidgetSharePreview&st.shareUrl=www.rambler.ru"]]];
    [self removeActivityIdicator];

    if (!User.ok_api){
        User.ok_api = [[Odnoklassniki alloc] initWithAppId:ok_appId appSecret:ok_appSecret appKey:ok_appKey delegate:self];
    }
    
    if (User.ok_api.isSessionValid){
        [self sharePostByOK];
    } else {
        [User.ok_api authorizeWithPermissions:@[@"VALUABLE ACCESS"]];
    }
    
    
    
    
    
    
//    
//webView = [[UIWebView alloc] initWithFrame:self.view.bounds];
//webView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
//webView.delegate = self;
//    UIButton *closeBtn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
//    [closeBtn setTitle:@"X" forState:UIControlStateNormal];
//    [closeBtn addTarget:self action:@selector(removeWebView) forControlEvents:UIControlEventTouchUpInside];
//    [webView addSubview:closeBtn];
//[self.view addSubview:webView];
//
//NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"http://connect.ok.ru/dk?st.cmd=WidgetSharePreview&st.shareUrl=www.rambler.ru"]];
//NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
//NSArray *cooks  = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
////    [Global addCookies:cooks forRequest:request];
//
//[webView loadRequest:request];
}


- (void)okShouldPresentAuthorizeController:(UIViewController *)viewController {
    [self presentViewController:viewController animated:YES completion:nil];
}

- (void)okWillDismissAuthorizeControllerByCancel:(BOOL)canceled {
    NSLog(@"autorization canceled by user");
}


/*
 * Method will be called after success login ([_api authorize:])
 * Метод будет вызван после успешной авторизации ([_api authorize:])
 */
- (void)okDidLogin {
    
    [self sharePostByOK];
}

/*
 * Method will be called if login faild (cancelled == YES if user cancelled login, NO otherwise)
 * Метод будет вызван, если при авторизации произошла ошибка (cancelled == YES если пользователь прервал авторизацию, NO во всех остальных случаях)
 */
- (void)okDidNotLogin:(BOOL)canceled {
    
}

/*
 * Method will be called if login faild and server returned an error
 * Метод будет вызван, если сервер вернул ошибку авторизации
 */
- (void)okDidNotLoginWithError:(NSError *)error {
    
}

/*
 * Method will be called if [_api refreshToken] called and new access_token was got
 * Метод будет вызван в случае, если вызван [_api refreshToken] и получен новый access_token
 */
- (void)okDidExtendToken:(NSString *)accessToken {
    [self okDidLogin];
}

/*
 * Method will be called if [_api refreshToken] called and new access_token wasn't got
 * Метод будет вызван в случае, если вызван [_api refreshToken] и новый access_token не получен
 */
- (void)okDidNotExtendToken:(NSError *)error {
    
}

/*
 * Method will be called after logout ([_api logout])
 * Метод будет вызван после выхода пользователя ([_api logout])
 */
- (void)okDidLogout {
    //    self.sessionStatusLabel.text = @"Not logged in";
    //    [self.authButton setTitle:@"Login" forState:UIControlStateNormal];
}


-(void)removeWebView{
    [webView removeFromSuperview];
}

#pragma mark - UIWebViewDelegate

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {

//    NSString *rUr;
//    if ([request.URL.absoluteString hasPrefix:rUr]) {
//        return YES;
//    }
    return YES;
}

-(void)webViewDidFinishLoad:(UIWebView *)webView{
    
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
    if (error.code == NSURLErrorCancelled) {
        return;
    }
    
    [[[UIAlertView alloc] initWithTitle:nil
                                message:@"No internet connection available"
                               delegate:self
                      cancelButtonTitle:@"Cancel"
                      otherButtonTitles:@"Repeat", nil] show];
    
}




-(void)shareByGp:(NSMutableDictionary*)post{
    postForShare = post;
    [self checkForGpAuth];
}


#pragma mark - FB 

-(void)requestFbPublishPermissionsAndPublish{
    if ([FBSession.activeSession.permissions indexOfObject:@"publish_actions"] == NSNotFound) {
        [[FBSession activeSession] requestNewPublishPermissions:@[@"publish_actions"]
                                                defaultAudience:FBSessionDefaultAudienceFriends
                                              completionHandler:^(FBSession *session, NSError *error) {
                                                  if (error){
                                                      [self failMessage];
                                                  } else {
                                                      [self sharePostByFb];
                                                  }
                                              }];
    } else {
        [self sharePostByFb];
    }
}
    
-(void)sharePostByFb{
    
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                   [self urlToShare], @"link",
                                   nil];
    
    // Publish.
    // This is the most important method that you call. It does the actual job, the message posting.
    [FBRequestConnection startWithGraphPath:@"me/feed"
                                 parameters:params
                                 HTTPMethod:@"POST"
                          completionHandler:^(FBRequestConnection *connection,
                                              id result,
                                              NSError *error)
     {
         if (error)
         {
             //showing an alert for failure
             [self failMessage];
         }
         else
         {
             //showing an alert for success
             [self successMessage];
         }
     }];
    
    
// with image
//    UIImage *image = [UIImage imageNamed:postForShare[@"image"]];
//    NSString *text = [NSString stringWithFormat:@"%@\n\n%@",postForShare[@"title"],postForShare[@"text"]];
////NSString *text = @"Your custom message";
//
//    NSMutableDictionary *params = [NSMutableDictionary new];
//    NSString* postToWallLink = @"me/photos";
//    //  UIImage *image = [imgAttachments objectAtIndex:0];
//    
//    NSData *imageData = UIImagePNGRepresentation(image);
//
//    [params setObject:text forKey:@"message"];
//    [params setObject:FBSession.activeSession.accessTokenData.accessToken forKey:@"access_token"];
//
//    
//    [params setObject:imageData forKey:@"picture"];
//
//    
//    [FBRequestConnection startWithGraphPath:postToWallLink
//                                 parameters:params
//                                 HTTPMethod:@"POST"
//                          completionHandler:^(FBRequestConnection *connection,
//                                              id result,
//                                              NSError *error)
//     {
//         if (error)
//         {
//             //showing an alert for failure
//             [self failMessage];
//         }
//         else
//         {
//             //showing an alert for success
//             [self successMessage];
//         }
//     }];
}


#pragma mark - google+
-(void)checkForGpAuth{
    
    [GPPSignIn sharedInstance].clientID = kGoogleClientIDKey;
    [GPPSignIn sharedInstance].delegate = self;
    [GPPSignIn sharedInstance].shouldFetchGoogleUserEmail = YES;
    [GPPSignIn sharedInstance].shouldFetchGoogleUserID = YES;
    //    [GPPSignIn sharedInstance].scopes = [NSArray arrayWithObjects:
    //                                         @"https://www.googleapis.com/auth/plus.login",
    //                                         @"https://www.googleapis.com/auth/plus.me",
    //                                         @"https://www.googleapis.com/auth/userinfo.profile",
    //                                         @"https://www.googleapis.com/auth/userinfo.email", nil];
    //   [GPPSignIn sharedInstance].scopes = @[ @"profile" ];
    //  [GPPSignIn sharedInstance].scopes= [NSArray arrayWithObjects:kGTLAuthScopePlusLogin, kGTLAuthScopePlusMe, nil];
    
    BOOL authenticated = ([GPPSignIn sharedInstance].authentication != nil);
    if (authenticated) {
        [self sharePostByGp];
    } else {
      //  [[GPPSignIn sharedInstance] signOut];
        [[GPPSignIn sharedInstance] authenticate];
    }
    
    
}

-(void)finishedWithAuth:(GTMOAuth2Authentication *)auth error:(NSError *)error{
//    gpId = [GPPSignIn sharedInstance].userID;
//    gpToken = auth.accessToken;
    [self sharePostByGp];
}


-(void)sharePostByGp{
    [self removeActivityIdicator];
    id<GPPShareBuilder> shareBuilder = [[GPPShare sharedInstance] nativeShareDialog];
    [shareBuilder setPrefillText:[NSString stringWithFormat:@"%@",postForShare[@"title"]]];
    [shareBuilder setURLToShare:[NSURL URLWithString:[self urlToShare]]];
//    [shareBuilder setContentDeepLinkID:@"my_id_here"];
//    [shareBuilder setTitle:postForShare[@"title"] description:postForShare[@"text"] thumbnailURL:[NSURL URLWithString:@"https://pp.vk.me/c624919/v624919258/223b6/cxyAYPJ-NUU.jpg"]];
    [shareBuilder open];
}


-(NSString*)urlToShare{
    int index = [postForShare[@"index"] intValue];
    return [NSString stringWithFormat:@"http://cardiomagnyl.dev.iengage.ru/good-to-know?blockId=%i#%i",index, index];
}

-(void)successMessage{
    [self removeActivityIdicator];
    [self showMessage:@"Запись успешно опубликована" title:@"Успех"];
}

-(void)failMessage{
    [self removeActivityIdicator];
    [self showMessage:@"Не получилось поделиться" title:@"Неудача"];
}



@end
