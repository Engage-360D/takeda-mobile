//
//  VControllerExt.m
//  takeda
//
//  Created by Alexander Rudenko on 12.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "VControllerExt.h"
#import "Personal.h"
#import "CalendarPage.h"

@interface VControllerExt ()

@end

@implementation VControllerExt

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.mainElement = self.view;
    self.navigationController.navigationBarHidden = NO;
    [self setNavigationPanel];
    [self setupData];
    [self setup_interface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self subscribeNotif];
    if (!self.parentVC){
        NSArray *controllers = self.navigationController.viewControllers;
        if (controllers.count>1){
            self.parentVC = controllers[controllers.count-2];
        } else if (controllers.count==1){
            self.parentVC = controllers[0];
        } else {
            self.parentVC = nil;
        }
    }
    
    [self setNavImage];
    [self autosetupTextFieldDelegates:self.view];
    [self updateCalendarBadge];
    self.navigationController.navigationBarHidden = NO;
}

-(void)viewDidDisappear:(BOOL)animated{
    [self unsubscribeNotif];
    self.isFromMenu = NO;
    self.isAppearFromBack = NO;
}

-(void)subscribeNotif{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(updateCalendarBadge)
                                                 name:kCalendarTasksChanged
                                               object:nil];
}

-(void)unsubscribeNotif{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)drawBorders:(NSArray*)array{
    for (UIView *view in array) {
        [self drawBordersInView:view];
    }

}

-(void)drawBordersInView:(UIView*)view{
    float sepH = 0.5f;
    CALayer *TopBorder = [CALayer layer];
    TopBorder.frame = CGRectMake(0.0f, 0.0f, view.frame.size.width, sepH);
    TopBorder.backgroundColor = RGB(178, 178, 178).CGColor;
    [view.layer addSublayer:TopBorder];
    CALayer *BottomBorder = [CALayer layer];
    BottomBorder.frame = CGRectMake(0.0f, view.frame.size.height-sepH, view.frame.size.width, sepH);
    BottomBorder.backgroundColor = RGB(178, 178, 178).CGColor;
    [view.layer addSublayer:BottomBorder];
}

-(void)setupData{
    _danger_text.text = @"Имеются противопоказания \n необходимо ознакомиться с инструкцией по применению";
}

-(void)setup_interface{
    [self drawBorders:self.bg_block];
    self.danger_text.numberOfLines = 0;
    self.danger_text.font = [UIFont fontWithName:@"SegoeWP" size:10];
    self.danger_text.textColor = RGB(55, 64, 76);
    self.vcTitle.font = [UIFont fontWithName:@"SegoeWP-Light" size:25];
    self.vcSubTitle.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    for (UIImageView *separ in self.separators_collection){
        separ.height = 0.5f;
        separ.y += 0.5;
    }

    //SegoeWP Light
}

#pragma mark - navigation panel
-(void)setNavImage{
    UINavigationBar *navBar = [[self navigationController] navigationBar];
    UIImage *backgroundImage;
    backgroundImage = [[UIImage imageNamed:@"nav_bar_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 10)] ;
    [navBar setBackgroundImage:backgroundImage forBarMetrics:UIBarMetricsDefault];
}

-(void)setNavigationPanel{
    UIView *view = [[UIView alloc] initWithFrame:[self.navigationController.navigationBar frame]];
    
    UIImage *logoImage = [UIImage imageNamed:@"title_logo"];
    
    
    UIImageView *img_logo = [[UIImageView alloc] initWithFrame:CGRectMake(40, 8, logoImage.size.width, logoImage.size.height)];
    img_logo.image = logoImage;
    [view addSubview:img_logo];
    view.backgroundColor = [UIColor clearColor];
    self.navigationItem.titleView = view;
    
    UIBarButtonItem *btn1 = [self personalButton];
    UIBarButtonItem *btn2 = [self alarmButton];

    // btn1.enabled = !(User.userBlocked);
    btn2.enabled = !(User.userBlocked);

    self.navigationItem.rightBarButtonItems = @[btn1,btn2];
    if (self.isRootVC){
        self.navigationItem.leftBarButtonItem = [self menuButton];
    } else {
        self.navigationItem.leftBarButtonItem = [self backBtn];
    }

    
}


-(void)openLeftMenu{
    if ([self.slideMenuController isMenuOpen]) {
        [self.slideMenuController closeMenuAnimated:YES completion:nil];
    }else{
        [self hideKeyb];
        [self.slideMenuController openMenuAnimated:YES completion:nil];
    }
}

-(void)backAction{
    [self.navigationController popViewControllerAnimated:YES];
    if (self.parentVC!=nil&&[self.parentVC respondsToSelector:@selector(setIsAppearFromBack:)]){
        [(VControllerExt*)self.parentVC setIsAppearFromBack:YES];
    }
    
//    NSArray *controllers = self.navigationController.viewControllers;
//    if (controllers.count>1){
//        if ([controllers[controllers.count-2] isKindOfClass:[VControllerExt class]]&&[controllers[controllers.count-2] respondsToSelector:@selector(setIsAppearFromBack:)]){
//            [(VControllerExt*)controllers[controllers.count-2] setIsAppearFromBack:YES];
//        }
//    }
}


-(UIImageView*)separatorLine{
    UIImageView *sp = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.view.width, 0.5f)];
    sp.height = 0.5f;
    sp.backgroundColor = RGB(170, 170, 170);
    return sp;
}

-(UIBarButtonItem*)menuButton{
    UIImage *menuImage = [UIImage imageNamed:@"menu_icon"];
    UIButton *aButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [aButton setImage:menuImage forState:UIControlStateNormal];
    aButton.frame = CGRectMake(0.0,0.0,menuImage.size.width+20,menuImage.size.height);
    aButton.contentEdgeInsets = (UIEdgeInsets){.left=-20};
    [aButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:aButton];
}

-(UIBarButtonItem*)personalButton{
    UIImage *peopleImage = [UIImage imageNamed:@"people_icon"];
    UIImage *peopleImageSel = [UIImage imageNamed:@"peopleIconSel"];
    UIButton *bButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [bButton setImage:peopleImage forState:UIControlStateNormal];
    [bButton setImage:peopleImageSel forState:UIControlStateSelected];
    

    bButton.frame = CGRectMake(0.0,0.0,peopleImage.size.width+10,peopleImage.size.height);
    bButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    [bButton addTarget:self action:@selector(showPersonal) forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:bButton];
}

-(UIBarButtonItem*)highlightedPersonalButton{
    UIImage *peopleImage = [UIImage imageNamed:@"people_icon"];
    UIImage *peopleImageSel = [UIImage imageNamed:@"peopleIconSel"];
    UIButton *bButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [bButton setImage:peopleImageSel forState:UIControlStateNormal];
    [bButton setImage:peopleImageSel forState:UIControlStateDisabled];
    
    bButton.frame = CGRectMake(0.0,0.0,peopleImage.size.width+10,peopleImage.size.height);
    bButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    [bButton addTarget:self action:@selector(showPersonal) forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:bButton];
}


-(UIBarButtonItem*)alarmButton{
    UIImage *alarmImage = [UIImage imageNamed:@"alarm_icon"];
    UIButton *cButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cButton setImage:alarmImage forState:UIControlStateNormal];
    cButton.frame = CGRectMake(0.0,0.0,alarmImage.size.width+10,alarmImage.size.height);
    cButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    [cButton addTarget:self action:@selector(showCalendar) forControlEvents:UIControlEventTouchUpInside];
    [cButton addSubview:self.calendarMissedEventsLabel];
    return [[UIBarButtonItem alloc] initWithCustomView:cButton];
}

-(UIBarButtonItem*)backBtn{
    UIButton *leftButtonItem = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 48, 30)];
    [leftButtonItem setImage:[UIImage imageNamed:@"back_btn"] forState:UIControlStateNormal];
    leftButtonItem.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [leftButtonItem addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:leftButtonItem];
}

- (UIBarButtonItem*)menuBarBtnWithImageName:(NSString*)imageName selector:(SEL)aSelector forTarget:(id)target{
    UIButton *menuButtonItem = [[UIButton alloc] initWithFrame:CGRectMake(0, 3, 30, 30)];
    [menuButtonItem setImage:[UIImage imageNamed:imageName] forState:UIControlStateNormal];
    menuButtonItem.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [menuButtonItem addTarget:target action:aSelector forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:menuButtonItem];
    
}

- (UIBarButtonItem*)menuBarBtnWithTitle:(NSString*)titleString selector:(SEL)aSelector forTarget:(id)target{
    UIButton *menuButtonItem = [[UIButton alloc] initWithFrame:CGRectMake(0, 3, 30, 30)];
    [menuButtonItem setTitle:titleString forState:UIControlStateNormal];
    [menuButtonItem.titleLabel setFont:[UIFont systemFontOfSize:17]];
    menuButtonItem.titleLabel.textColor = [UIColor clearColor];
    menuButtonItem.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [menuButtonItem addTarget:target action:aSelector forControlEvents:UIControlEventTouchUpInside];
    [menuButtonItem setFrame:CGRectMake(menuButtonItem.frame.origin.x, menuButtonItem.frame.origin.y, [menuButtonItem sizeThatFits:CGSizeZero].width, menuButtonItem.frame.size.height)];
    return [[UIBarButtonItem alloc] initWithCustomView:menuButtonItem];
    
}

-(UILabel*)calendarMissedEventsLabel{
    if (!_calendarMissedEventsLabel){
        _calendarMissedEventsLabel = [[UILabel alloc] init];
    }
    [self updateCalendarBadge];
    return _calendarMissedEventsLabel;
}

-(void)updateCalendarBadge{
    if (User.userData==nil||GData.missedEventsCount==nil) return;
    NSString *calendarCount = [NSString stringWithFormat:@"%i", [GData.missedEventsCount intValue]];
    CGRect badgeFrame = CGRectMake(23, -2, 0, 15);
    _calendarMissedEventsLabel.text = calendarCount;
    _calendarMissedEventsLabel.textAlignment = NSTextAlignmentCenter;
    _calendarMissedEventsLabel.contentMode = UIViewContentModeCenter;
    _calendarMissedEventsLabel.font = [UIFont fontWithName:@"Helvetica" size:12];
    _calendarMissedEventsLabel.textColor = [UIColor whiteColor];
    _calendarMissedEventsLabel.backgroundColor = RGB(213, 30, 39);
    _calendarMissedEventsLabel.layer.cornerRadius = badgeFrame.size.height/2;
    _calendarMissedEventsLabel.clipsToBounds = YES;
    CGSize textSize = [Global text:[_calendarMissedEventsLabel text] sizeWithFont:[_calendarMissedEventsLabel font] constrainedToSize:CGSizeMake(CGFLOAT_MAX, _calendarMissedEventsLabel.height)];
    badgeFrame.size.width = MAX(calendarCount.length>1?textSize.width+6:textSize.width,badgeFrame.size.height);
    _calendarMissedEventsLabel.hidden = (calendarCount.length == 0||[GData.missedEventsCount intValue] == 0);
    _calendarMissedEventsLabel.frame = badgeFrame;
}

- (BOOL)isModal {
    if([self presentingViewController])
        return YES;
    if([[self presentingViewController] presentedViewController] == self)
        return YES;
    if([[[self navigationController] presentingViewController] presentedViewController] == [self navigationController])
        return YES;
    if([[[self tabBarController] presentingViewController] isKindOfClass:[UITabBarController class]])
        return YES;
    
    return NO;
}

-(BOOL)isRootVC{
    return  self == [self.navigationController.viewControllers objectAtIndex: 0];
}

-(void)showPersonal{
    Personal *personal = [Personal new];
    [self.navigationController pushViewController:personal animated:YES];
}

-(void)showCalendar{
    CalendarPage *calendar = [CalendarPage new];
    [self.navigationController pushViewController:calendar animated:YES];
}

#pragma mark keyb


-(void)autosetupTextFieldDelegates:(UIView *)view {
    NSArray *subviews = [view subviews];
    if ([subviews count] == 0) return;
    for (UIView *subview in subviews) {
        if ([subview isKindOfClass:[UITextField class]]){
            UITextField *f = (UITextField*)subview;
            if (!f.delegate) f.delegate = self; // !!!! Original  - f.delegate = self;
        } else if ([subview isKindOfClass:[UITextView class]]){
            UITextView *f = (UITextView*)subview;
            if (!f.delegate) f.delegate = self;
        } else {
            [self autosetupTextFieldDelegates:subview];
        }
    }}


-(void)keybHided:(id)sender{
    //  UIView *a = (UIView*)sender;
    //  [a.superview removeGestureRecognizer:tapToHide];
    [appDelegate.window removeGestureRecognizer:appDelegate.tapToHide];
    
}

-(void)keybShowed:(id)sender{
    appDelegate.tapToHide = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(hideKeybTap:)];
    appDelegate.tapToHide.delegate = self;
    //  UIView *a = (UIView*)sender;
    //   [a.superview addGestureRecognizer:tapToHide];
    [appDelegate.window addGestureRecognizer:appDelegate.tapToHide];
    
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch {
    if([touch.view isKindOfClass:[UITableViewCell class]]) {
        return NO;
    }
    // UITableViewCellContentView => UITableViewCell
    if([touch.view.superview isKindOfClass:[UITableViewCell class]]) {
        return NO;
    }
    // UITableViewCellContentView => UITableViewCellScrollView => UITableViewCell
    if([touch.view.superview.superview isKindOfClass:[UITableViewCell class]]) {
        return NO;
    }
    return YES; // handle the touch
}

//
//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch {
//    // test if our control subview is on-screen
//    
//    if ([touch.view isKindOfClass:[UITableView class]]&&[touch.view isKindOfClass:[UITableViewCell class]]){
//        return NO;
//    }
//    
//    return YES; // handle the touch
//}


-(void)hideKeybTap:(UITapGestureRecognizer*)sender{
    [self hideKeyb];
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}
#pragma mark-

///////////////////////////////////////////////////////////////// - Поднятие экрана при появлении клавиатуры

CGFloat animatedDistance;
static const CGFloat KEYBOARD_ANIMATION_DURATION = 0.3;
static const CGFloat MINIMUM_SCROLL_FRACTION = 0.2;
static const CGFloat MAXIMUM_SCROLL_FRACTION = 0.8;
static const CGFloat PORTRAIT_KEYBOARD_HEIGHT = 216;
static const CGFloat LANDSCAPE_KEYBOARD_HEIGHT = 162;

-(void) textFieldDidBeginEditing:(UITextField *)textField
{
    Global.keybHolder = textField;
    if ([self respondsToSelector:@selector(keybShowed:)]){
        [self keybShowed:textField];
    }
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        CGRect textFieldRect = [self.view.window convertRect:textField.bounds fromView:textField];
        CGRect viewRect = [self.view.window convertRect:self.view.bounds fromView:self.view];
        CGFloat midline = textFieldRect.origin.y + 0.5 * textFieldRect.size.height;
        CGFloat numerator = midline - viewRect.origin.y - MINIMUM_SCROLL_FRACTION * viewRect.size.height;
        CGFloat denominator = (MAXIMUM_SCROLL_FRACTION - MINIMUM_SCROLL_FRACTION) * viewRect.size.height;
        CGFloat heightFraction = numerator / denominator;
        
        if (heightFraction < 0.0)
        {
            heightFraction = 0.0;
        }
        else if (heightFraction > 1.0)
        {
            heightFraction = 1.0;
        }
        
        UIInterfaceOrientation orientation = [[UIApplication sharedApplication] statusBarOrientation];
        if (orientation == UIInterfaceOrientationPortrait)
        {
            animatedDistance = floor(PORTRAIT_KEYBOARD_HEIGHT * heightFraction);
        }
        else
        {
            animatedDistance = floor(LANDSCAPE_KEYBOARD_HEIGHT * heightFraction);
        }
        
        CGRect viewFrame = self.view.frame;
        viewFrame.origin.y -= animatedDistance;
        
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationBeginsFromCurrentState:YES];
        [UIView setAnimationDuration:KEYBOARD_ANIMATION_DURATION];
        
        [self.view setFrame:viewFrame];
        
        [UIView commitAnimations];
    }
}

-(void) textFieldDidEndEditing:(UITextField *)textField
{
    Global.keybHolder = nil;
    if ([self respondsToSelector:@selector(keybHided:)]){
        [self keybHided:textField];
    }
    if(UI_USER_INTERFACE_IDIOM()==UIUserInterfaceIdiomPhone)
    {
        CGRect viewFrame = self.view.frame;
        viewFrame.origin.y += animatedDistance;
        
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationBeginsFromCurrentState:YES];
        [UIView setAnimationDuration:KEYBOARD_ANIMATION_DURATION];
        
        [self.view setFrame:viewFrame];
        
        [UIView commitAnimations];
    }
}

-(void) textViewDidBeginEditing:(UITextView *)textView
{
    Global.keybHolder = textView;
    if ([self respondsToSelector:@selector(keybShowed:)]){
        [self keybShowed:textView];
    }
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        CGRect textViewRect = [self.view.window convertRect:textView.bounds fromView:textView];
        CGRect viewRect = [self.view.window convertRect:self.view.bounds fromView:self.view];
        CGFloat midline = textViewRect.origin.y + 0.5 * textViewRect.size.height;
        CGFloat numerator = midline - viewRect.origin.y - MINIMUM_SCROLL_FRACTION * viewRect.size.height;
        CGFloat denominator = (MAXIMUM_SCROLL_FRACTION - MINIMUM_SCROLL_FRACTION) * viewRect.size.height;
        CGFloat heightFraction = numerator / denominator;
        
        if (heightFraction < 0.0)
        {
            heightFraction = 0.0;
        }
        else if (heightFraction > 1.0)
        {
            heightFraction = 1.0;
        }
        
        UIInterfaceOrientation orientation = [[UIApplication sharedApplication] statusBarOrientation];
        if (orientation == UIInterfaceOrientationPortrait)
        {
            animatedDistance = floor(PORTRAIT_KEYBOARD_HEIGHT * heightFraction);
        }
        else
        {
            animatedDistance = floor(LANDSCAPE_KEYBOARD_HEIGHT * heightFraction);
        }
        
        CGRect viewFrame = self.view.frame;
        viewFrame.origin.y -= animatedDistance;
        
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationBeginsFromCurrentState:YES];
        [UIView setAnimationDuration:KEYBOARD_ANIMATION_DURATION];
        
        [self.view setFrame:viewFrame];
        
        [UIView commitAnimations];
    }
}

-(void) textViewDidEndEditing:(UITextView *)textView
{
    Global.keybHolder = nil;
    if ([self respondsToSelector:@selector(keybHided:)]){
        [self keybHided:textView];
    }
    
    if(UI_USER_INTERFACE_IDIOM()==UIUserInterfaceIdiomPhone)
    {
        CGRect viewFrame = self.view.frame;
        viewFrame.origin.y += animatedDistance;
        
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationBeginsFromCurrentState:YES];
        [UIView setAnimationDuration:KEYBOARD_ANIMATION_DURATION];
        
        [self.view setFrame:viewFrame];
        
        [UIView commitAnimations];
    }
}


//////////////////////////////////////////////////////////////// - Скрыть клавиатуру по нажатию кнопки



@end
