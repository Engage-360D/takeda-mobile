/*
 * RNBlurModal
 *
 * Created by Ryan Nystrom on 10/2/12.
 * Copyright (c) 2012 Ryan Nystrom. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

#import "RNBlurModalView.h"
#import <Accelerate/Accelerate.h>
#import <QuartzCore/QuartzCore.h>

/*
    This bit is important! In order to prevent capturing selected states of UIResponders I've implemented a delay. Please feel free to set this delay to *whatever* you deem apprpriate.
    I've defaulted it to 0.125 seconds. You can do shorter/longer as you see fit. 
 */
CGFloat const kRNBlurDefaultDelay = 0.125f;

/*
    You can also change this constant to make the blur more "blurry". I recommend the tasteful level of 0.2 and no higher. However, you are free to change this from 0.0 to 1.0.
 */
CGFloat const kRNDefaultBlurScale = 0.2f;

CGFloat const kRNBlurDefaultDuration = 0.2f;
CGFloat const kRNBlurViewMaxAlpha = 1.f;

CGFloat const kRNBlurBounceOutDurationScale = 0.8f;

CGFloat const kDefaultAutocloseDelay = 2.0f;



NSString * const kRNBlurDidShowNotification = @"com.whoisryannystrom.RNBlurModalView.show";
NSString * const kRNBlurDidHidewNotification = @"com.whoisryannystrom.RNBlurModalView.hide";

typedef void (^RNBlurCompletion)(void);

@interface UILabel (AutoSize)
- (void)autoHeight;
@end

@interface UIView (Sizes)
@property (nonatomic) CGFloat left;
@property (nonatomic) CGFloat top;
@property (nonatomic) CGFloat right;
@property (nonatomic) CGFloat bottom;
@property (nonatomic) CGFloat width;
@property (nonatomic) CGFloat height;
@property (nonatomic) CGPoint origin;
@property (nonatomic) CGSize size;
@end

@interface UIView (Screenshot)
- (UIImage*)screenshot;
@end

@interface UIImage (Blur)
-(UIImage *)boxblurImageWithBlur:(CGFloat)blur;
@end

@interface RNBlurView : UIImageView
- (id)initWithCoverView:(UIView*)view;
@end

@interface RNCloseButton : UIButton
@end

@interface RNBlurModalView ()
@property (assign, readwrite) BOOL isVisible;
@end

#pragma mark - RNBlurModalView

@implementation RNBlurModalView {
    UIViewController *_controller;
    UIView *_parentView;
    UIView *_contentView;
    RNCloseButton *_dismissButton;
    RNBlurView *_blurView;
    RNBlurCompletion _completion;
    UITapGestureRecognizer *tapToClose;
}

+ (UIView*)generateModalViewWithTitle:(NSString*)title message:(NSString*)message {
    CGFloat defaultWidth = 280.f;
    CGRect frame = CGRectMake(0, 0, defaultWidth, 0);
    CGFloat padding = 10.f;
    UIView *view = [[UIView alloc] initWithFrame:frame];
    
    UIColor *whiteColor = [UIColor colorWithRed:0.816 green:0.788 blue:0.788 alpha:1.000];
    
    view.backgroundColor = [UIColor colorWithWhite:0.1 alpha:0.8f];
    view.layer.borderColor = whiteColor.CGColor;
    view.layer.borderWidth = 2.f;
    view.layer.cornerRadius = 10.f;
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(padding, 0, defaultWidth - padding * 2.f, 0)];
    titleLabel.text = title;
    titleLabel.font = [UIFont fontWithName:@"SegoeWP" size:17.0];
    titleLabel.textColor = [UIColor whiteColor];
    titleLabel.shadowColor = [UIColor blackColor];
    titleLabel.shadowOffset = CGSizeMake(0, -1);
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.backgroundColor = [UIColor clearColor];
    [titleLabel autoHeight];
    titleLabel.numberOfLines = 0;
    titleLabel.top = padding;
    [view addSubview:titleLabel];
    if (title == nil||title.length == 0){
        titleLabel.height = 0;
        titleLabel.y = 0;
    }
    
    UILabel *messageLabel = [[UILabel alloc] initWithFrame:CGRectMake(padding, 0, defaultWidth - padding * 2.f, 0)];
    messageLabel.text = message;
    messageLabel.numberOfLines = 0;
    messageLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17.0];
    messageLabel.textColor = titleLabel.textColor;
    messageLabel.shadowOffset = titleLabel.shadowOffset;
    messageLabel.shadowColor = titleLabel.shadowColor;
    messageLabel.textAlignment = NSTextAlignmentCenter;
    messageLabel.backgroundColor = [UIColor clearColor];
    [messageLabel autoHeight];
    messageLabel.top = titleLabel.bottom + padding;
    [view addSubview:messageLabel];
    
    view.height = messageLabel.bottom + padding;
    
    view.autoresizingMask = UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleBottomMargin | UIViewAutoresizingFlexibleRightMargin;
    
    return view;
}

+ (UIView*)generateModalViewWithAutoTitle:(NSString*)title message:(NSString*)message {
    CGFloat defaultWidth = 200.f;
    CGRect frame = CGRectMake(0, 0, defaultWidth, 0);
    CGFloat padding = 10.f;
    UIView *view = [[UIView alloc] initWithFrame:frame];
    
    
    view.backgroundColor = [UIColor whiteColor];
    view.layer.borderWidth = 0.f;
    view.layer.cornerRadius = 5.f;
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(padding, 0, defaultWidth - padding * 2.f, 0)];
    titleLabel.text = title;
    titleLabel.font = [UIFont systemFontOfSize:16.f];
    titleLabel.textColor = [UIColor blackColor];
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.backgroundColor = [UIColor clearColor];
    [titleLabel autoHeight];
    titleLabel.numberOfLines = 0;
    titleLabel.top = padding;
    [view addSubview:titleLabel];
    
    UILabel *messageLabel = [[UILabel alloc] initWithFrame:CGRectMake(padding, 0, defaultWidth - padding * 2.f, 0)];
    messageLabel.text = message;
    messageLabel.numberOfLines = 0;
    messageLabel.font = [UIFont systemFontOfSize:13.f];
    messageLabel.textColor = titleLabel.textColor;
      messageLabel.textAlignment = NSTextAlignmentCenter;
    messageLabel.backgroundColor = [UIColor clearColor];
    [messageLabel autoHeight];
    messageLabel.top = titleLabel.bottom + padding;
    [view addSubview:messageLabel];
    
    view.height = messageLabel.bottom + padding;
    
    view.autoresizingMask = UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleBottomMargin | UIViewAutoresizingFlexibleRightMargin;
    
    return view;
}

+ (UIView*)generateModalViewWithAlertMenuTitle:(NSString*)title cancelBtn:(NSDictionary*)cancelBtn otherButtons:(NSArray*)otherBtns aligment:(UIControlContentHorizontalAlignment)aligment{
    CGFloat btnHeight = 44;
    CGFloat btnWidth = 250;
    CGFloat btnDistance = 7;
    CGFloat btnCancelDistance = 30;
    CGFloat defaultWidth = 250.f;
    CGFloat bottomDistance = 72;
    
    CGFloat viewHeight = bottomDistance+((otherBtns.count+1)*btnHeight+(otherBtns.count-1)*btnDistance)+btnCancelDistance;
    
    CGRect frame = CGRectMake(0, 0, defaultWidth, viewHeight);
    UIView *view = [[UIView alloc] initWithFrame:frame];
    
    view.backgroundColor = [UIColor clearColor];

    for (int i = 1; i<=otherBtns.count; i++){
        UIButton *menuItem = [self menuBtnWithTitle:[[otherBtns objectAtIndex:i-1] objectForKey:@"title"] image:[[otherBtns objectAtIndex:i-1] objectForKey:@"image"] aligment:aligment];
        CGPoint center = CGPointMake(defaultWidth/2, (btnHeight+btnDistance)*i-btnHeight/2-btnDistance);
        menuItem.center = center;
        menuItem.tag = [[[otherBtns objectAtIndex:i-1] objectForKey:@"tag"] intValue];
        [view addSubview:menuItem];
        }
    
    UIButton *cancelButton = [self menuBtnWithTitle:[cancelBtn objectForKey:@"title"] image:[cancelBtn objectForKey:@"image"] aligment:aligment];

    cancelButton.center = CGPointMake(defaultWidth/2, viewHeight - bottomDistance-btnHeight/2);
    [view addSubview:cancelButton];
    
//    view.autoresizingMask = UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleBottomMargin | UIViewAutoresizingFlexibleRightMargin;
    
    return view;
}

+(UIButton*)menuBtnWithTitle:(NSString*)title image:(NSString*)image aligment:(UIControlContentHorizontalAlignment)aligment{
    CGFloat btnHeight = 44;
    CGFloat btnWidth = 250;
    CGRect frameBtn = CGRectMake(0, 0, btnWidth, btnHeight);
    UIButton *btn = [[UIButton alloc] initWithFrame:frameBtn];
    btn.backgroundColor = [UIColor colorWithWhite:0.1 alpha:0.5];
    btn.layer.cornerRadius = 10.0f;
    [btn setContentHorizontalAlignment:aligment];

    if (aligment!=UIControlContentHorizontalAlignmentCenter){
    [btn setImageEdgeInsets:UIEdgeInsetsMake(0,17,0,0)];
    [btn setTitleEdgeInsets:UIEdgeInsetsMake(0,27,0,0)];
    } else {
        [btn setImageEdgeInsets:UIEdgeInsetsMake(0,-10,0,0)];
    }
    
    [btn.titleLabel setFont:[UIFont systemFontOfSize:20]];
    [btn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [btn setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
    
    if (image.length>1) {
        [btn setImage:[UIImage imageNamed:image] forState:UIControlStateNormal];
        NSString *selImName = [image stringByReplacingOccurrencesOfString:@".png" withString:@"Sel.png"];
        UIImage *selIm = [UIImage imageNamed:selImName];
        if (selIm)
            [btn setImage:selIm forState:UIControlStateHighlighted];
    }

    [btn setTitle:title forState:UIControlStateNormal];
    
    return btn;
}


+(UIImageView*)shadowViewFrame:(CGRect)frame{
    CGRect shadowFrame = CGRectMake(frame.origin.x-14, frame.origin.y-14, frame.size.width+28, frame.size.height+28);
    UIImageView *shadow = [[UIImageView alloc] initWithFrame:shadowFrame];
    shadow.image = [[UIImage imageNamed:@"shadow"] resizableImageWithCapInsets:UIEdgeInsetsMake(14, 14, 14, 14)];
    //shadow.backgroundColor = [UIColor redColor];
    shadow.alpha = 0.5f;
    return shadow;
}

- (id)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        _dismissButton = [[RNCloseButton alloc] init];
        _dismissButton.center = CGPointZero;
        [_dismissButton addTarget:self action:@selector(hide) forControlEvents:UIControlEventTouchUpInside];
        
        self.alpha = 0.f;
        self.backgroundColor = [UIColor clearColor];
//        self.backgroundColor = [UIColor redColor];
//        self.layer.borderWidth = 2.f;
//        self.layer.borderColor = [UIColor blackColor].CGColor;
        
        self.autoresizingMask = (UIViewAutoresizingFlexibleWidth |
                                  UIViewAutoresizingFlexibleHeight |
                                  UIViewAutoresizingFlexibleLeftMargin |
                                  UIViewAutoresizingFlexibleTopMargin);
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(orientationDidChangeNotification:) name:UIApplicationDidChangeStatusBarOrientationNotification object:nil];
    }
    return self;
}



- (id)initWithViewController:(UIViewController*)viewController view:(UIView*)view {
    if (self = [self initWithFrame:CGRectMake(0, 0, viewController.view.width, viewController.view.height)]) {
        [self addSubview:view];
        _contentView = view;
        _contentView.center = CGPointMake(CGRectGetMidX(self.frame), CGRectGetMidY(self.frame));
        _controller = viewController;
        _parentView = nil;
        _contentView.clipsToBounds = YES;
        _contentView.layer.masksToBounds = YES;
    }
    return self;
}


- (id)initWithViewController:(UIViewController*)viewController title:(NSString*)title message:(NSString*)message {
    UIView *view = [RNBlurModalView generateModalViewWithTitle:title message:message];
    if (self = [self initWithViewController:viewController view:view]) {
        _dismissButton.center = CGPointMake(view.left, view.top);
        [self addSubview:_dismissButton];
    }
    return self;
}

- (id)initWithParentView:(UIView*)parentView view:(UIView*)view {
    if (self = [self initWithFrame:CGRectMake(0, 0, parentView.width, parentView.height)]) {
        UIImageView *shadow = [RNBlurModalView shadowViewFrame:view.frame];
        if (self.shadowNeed) [self addSubview:shadow];
        
        [self addSubview:view];
        _controller = nil;

        _contentView = view;
        _parentView = parentView;
        _contentView.clipsToBounds = YES;
        _contentView.layer.masksToBounds = YES;

        if (self.msgType==mAlert) {
            _contentView.center = CGPointMake(CGRectGetMidX(self.frame), (self.frame.size.height-_contentView.frame.size.height/2));
        } else {
            _contentView.center = CGPointMake(CGRectGetMidX(self.frame), CGRectGetMidY(self.frame));
        }
        shadow.center = _contentView.center;
        }
    return self;
}

- (id)initWithParentView:(UIView*)parentView title:(NSString*)title message:(NSString*)message {
    UIView *view = [RNBlurModalView generateModalViewWithTitle:title message:message];
    if (self = [self initWithParentView:parentView view:view]) {
        _dismissButton.center = CGPointMake(view.left, view.top);
        [self addSubview:_dismissButton];
    }
    return self;
}


- (id)initWithView:(UIView*)view {
    if (self = [self initWithParentView:[[UIApplication sharedApplication].delegate window].rootViewController.view view:view]) {
        // nothing to see here
    }
    return self;
}

- (id)initWithTitle:(NSString*)title message:(NSString*)message {
    UIView *view = [RNBlurModalView generateModalViewWithTitle:title message:message];
    if (self = [self initWithView:view]) {
        // nothing to see here
        tapToClose = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(hide)];
        [self addGestureRecognizer:tapToClose];
    }
    return self;
}

-(void)hideFromTap:(UITapGestureRecognizer*)tepR{
    
}

#pragma mark Ruden
- (id)initAutocloseWithTitle:(NSString*)title message:(NSString*)message lifeTime:(int)lifeTime{
    self.msgType = mAutoclose;
    self.shadowNeed = YES;
    if (lifeTime) self.autoCloseInterval=lifeTime; else self.autoCloseInterval=kDefaultAutocloseDelay;
    self.autoClosed = YES;
    UIView *view = [RNBlurModalView generateModalViewWithAutoTitle:title message:message];
    
    if (self = [self initWithView:view]) {
        // nothing to see here
    }
    return self;
}

- (id)initAlertMenuTitle:(NSString*)title cancelBtn:(NSDictionary*)cancelBtn otherButtons:(NSArray*)otherBtns  aligment:(UIControlContentHorizontalAlignment)aligment{
    self.msgType = mAlert;
    self.autoClosed = NO;
    self.shadowNeed = NO;
    
    UIView *view = [RNBlurModalView generateModalViewWithAlertMenuTitle:title cancelBtn:cancelBtn otherButtons:otherBtns aligment:(UIControlContentHorizontalAlignment)aligment];
       
    for (id men in view.subviews) {
        if ([men isKindOfClass:[UIButton class]]){
            [men addTarget:self action:@selector(pressButtton:) forControlEvents:UIControlEventTouchUpInside];
        }
    }
    
    if (self = [self initWithView:view]) {
        
    }
    return self;
}



-(void)pressButtton:(UIButton*)sender{
    int index = sender.tag;
    self.btnPressed = index;
    [self hide];
}

-(void)destroy{
    [self hide];
}


- (void)layoutSubviews {
    [super layoutSubviews];
    
    CGFloat centerX = self.dismissButtonRight ? _contentView.right : _contentView.left;
    _dismissButton.center = CGPointMake(centerX, _contentView.top);
}

- (void)willMoveToSuperview:(UIView *)newSuperview {
    [super willMoveToSuperview:newSuperview];
    if (newSuperview) {
        self.center = CGPointMake(CGRectGetMidX(newSuperview.frame), CGRectGetMidY(newSuperview.frame));
    }
}


- (void)orientationDidChangeNotification:(NSNotification*)notification {
	if ([self isVisible]) {
		[self performSelector:@selector(updateSubviews) withObject:nil afterDelay:0.3f];
	}
}


- (void)updateSubviews {
    self.hidden = YES;
    
    // get new screenshot after orientation
    [_blurView removeFromSuperview]; _blurView = nil;
    if (_controller) {
        _blurView = [[RNBlurView alloc] initWithCoverView:_controller.view];
        _blurView.alpha = 1.f;
        [_controller.view insertSubview:_blurView belowSubview:self];

    }
    else if(_parentView) {
        _blurView = [[RNBlurView alloc] initWithCoverView:_parentView];
        _blurView.alpha = 1.f;
        [_parentView insertSubview:_blurView belowSubview:self];

    }
    
    
    
    self.hidden = NO;

    _contentView.center = CGPointMake(CGRectGetMidX(self.frame), CGRectGetMidY(self.frame));
    _dismissButton.center = _contentView.origin;
}


- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


- (void)show {
    
    /*
     CGPoint hidedCenter = CGPointMake(self.frame.size.width/2, self.frame.size.height+view.frame.size.height);
     CGPoint showedCenter = CGPointMake(self.frame.size.width/2, self.frame.size.height-view.frame.size.height/2);
     view.center = hidedCenter;
     [UIView animateWithDuration: 3.5 delay:0 options:UIViewAnimationCurveLinear animations:^{
     view.center = showedCenter;
     } completion:^(BOOL finished){
     
     }];

     */
    
    [self showWithDuration:kRNBlurDefaultDuration delay:0 options:kNilOptions completion:NULL];
    if (self.autoClosed){
        [self performSelector:@selector(destroy) withObject:nil afterDelay:self.autoCloseInterval];
    }
    
    
}


- (void)showWithDuration:(CGFloat)duration delay:(NSTimeInterval)delay options:(UIViewAnimationOptions)options completion:(void (^)(void))completion {
    self.animationDuration = duration;
    self.animationDelay = delay;
    self.animationOptions = options;
    _completion = [completion copy];
    
    // delay so we dont get button states
    [self performSelector:@selector(delayedShow) withObject:nil afterDelay:kRNBlurDefaultDelay];
}


- (void)delayedShow {
    if (! self.isVisible) {
        if (! self.superview) {
            if (_controller) {
                self.frame = CGRectMake(0, 0, _controller.view.bounds.size.width, _controller.view.bounds.size.height);
                [_controller.view addSubview:self];
            }
            else if(_parentView) {
                self.frame = CGRectMake(0, 0, _parentView.bounds.size.width, _parentView.bounds.size.height);

                [_parentView addSubview:self];
            }
            self.top = 0;
        }
        
        if (_controller) {
            _blurView = [[RNBlurView alloc] initWithCoverView:_controller.view];
            _blurView.alpha = 0.f;
            self.frame = CGRectMake(0, 0, _controller.view.bounds.size.width, _controller.view.bounds.size.height);

            [_controller.view insertSubview:_blurView belowSubview:self];
        }
        else if(_parentView) {
            _blurView = [[RNBlurView alloc] initWithCoverView:_parentView];
            _blurView.alpha = 0.f;
            self.frame = CGRectMake(0, 0, _parentView.bounds.size.width, _parentView.bounds.size.height);

            [_parentView insertSubview:_blurView belowSubview:self];
        }
        
        
        if (self.msgType == mAlert){
            self.alpha = 1.f;

            CGPoint hidedCenter = CGPointMake(self.frame.size.width/2, self.frame.size.height+self.frame.size.height);
            CGPoint showedCenter = CGPointMake(self.frame.size.width/2, self.frame.size.height/2);
            self.center = hidedCenter;
            [UIView animateWithDuration: 0.5 delay:0 options:UIViewAnimationCurveLinear animations:^{
                self.center = showedCenter;
                _blurView.alpha = 1.0f;
            } completion:^(BOOL finished){
                if (finished) {
                    [[NSNotificationCenter defaultCenter] postNotificationName:kRNBlurDidShowNotification object:nil];
                    self.isVisible = YES;
                    if (_completion) {
                        _completion();
                    }
                }

            }];

        } else {
            self.transform = CGAffineTransformScale(CGAffineTransformIdentity, 0.4, 0.4);

            [UIView animateWithDuration:self.animationDuration animations:^{
            _blurView.alpha = 1.0f;
            self.alpha = 1.f;
            self.transform = CGAffineTransformScale(CGAffineTransformIdentity, 1.f, 1.f);
        } completion:^(BOOL finished) {
            if (finished) {
                [[NSNotificationCenter defaultCenter] postNotificationName:kRNBlurDidShowNotification object:nil];
                self.isVisible = YES;
                if (_completion) {
                    _completion();
                }
            }
        }];
        }
    }

}


- (void)hide {
    [self hideWithDuration:kRNBlurDefaultDuration delay:0 options:kNilOptions completion:self.defaultHideBlock];
}


- (void)hideWithDuration:(CGFloat)duration delay:(NSTimeInterval)delay options:(UIViewAnimationOptions)options completion:(void (^)(int))completion {
    if (self.isVisible) {
        [UIView animateWithDuration:duration
                              delay:delay
                            options:options
                         animations:^{
                             self.alpha = 0.f;
                             _blurView.alpha = 0.f;
                         }
                         completion:^(BOOL finished){
                             if (finished) {
                                 [_blurView removeFromSuperview];
                                 _blurView = nil;
                                 [self removeFromSuperview];
                                 
                                 [[NSNotificationCenter defaultCenter] postNotificationName:kRNBlurDidHidewNotification object:nil];
                                 self.isVisible = NO;
                                 if (completion) {
                                     completion(self.btnPressed);
                                 }
                             }
                         }];
    }
}

-(void)hideCloseButton:(BOOL)hide {
    [_dismissButton setHidden:hide];
}

@end

#pragma mark - RNBlurView

@implementation RNBlurView {
    UIView *_coverView;
}

- (id)initWithCoverView:(UIView *)view {
    if (self = [super initWithFrame:CGRectMake(0, 0, view.bounds.size.width, view.bounds.size.height)]) {
        _coverView = view;
        UIImage *blur = [_coverView screenshot];
        self.image = [blur boxblurImageWithBlur:kRNDefaultBlurScale];
    }
    return self;
}


@end

#pragma mark - UILabel + Autosize

@implementation UILabel (AutoSize)

- (void)autoHeight {
    CGRect frame = self.frame;
    CGSize maxSize = CGSizeMake(frame.size.width, 9999);
    CGSize expectedSize = [self.text sizeWithFont:self.font constrainedToSize:maxSize lineBreakMode:self.lineBreakMode];
    frame.size.height = expectedSize.height;
    [self setFrame:frame];
}

@end

#pragma mark - UIView + Sizes

@implementation UIView (Sizes)

- (CGFloat)left {
    return self.frame.origin.x;
}

- (void)setLeft:(CGFloat)x {
    CGRect frame = self.frame;
    frame.origin.x = x;
    self.frame = frame;
}

- (CGFloat)top {
    return self.frame.origin.y;
}

- (void)setTop:(CGFloat)y {
    CGRect frame = self.frame;
    frame.origin.y = y;
    self.frame = frame;
}

- (CGFloat)right {
    return self.frame.origin.x + self.frame.size.width;
}

- (void)setRight:(CGFloat)right {
    CGRect frame = self.frame;
    frame.origin.x = right - frame.size.width;
    self.frame = frame;
}

- (CGFloat)bottom {
    return self.frame.origin.y + self.frame.size.height;
}

- (void)setBottom:(CGFloat)bottom {
    CGRect frame = self.frame;
    frame.origin.y = bottom - frame.size.height;
    self.frame = frame;
}

- (CGFloat)width {
    return self.frame.size.width;
}

- (void)setWidth:(CGFloat)width {
    CGRect frame = self.frame;
    frame.size.width = width;
    self.frame = frame;
}

- (CGFloat)height {
    return self.frame.size.height;
}

- (void)setHeight:(CGFloat)height {
    CGRect frame = self.frame;
    frame.size.height = height;
    self.frame = frame;
}

- (CGPoint)origin {
    return self.frame.origin;
}

- (void)setOrigin:(CGPoint)origin {
    CGRect frame = self.frame;
    frame.origin = origin;
    self.frame = frame;
}

- (CGSize)size {
    return self.frame.size;
}

- (void)setSize:(CGSize)size {
    CGRect frame = self.frame;
    frame.size = size;
    self.frame = frame;
}

@end

#pragma mark - RNCloseButton

@implementation RNCloseButton

- (id)init{
    if(!(self = [super initWithFrame:(CGRect){0, 0, 32, 32}])){
        return nil;
    }
    static UIImage *closeButtonImage;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        closeButtonImage = [self closeButtonImage];
    });
    [self setBackgroundImage:closeButtonImage forState:UIControlStateNormal];
    self.accessibilityTraits |= UIAccessibilityTraitButton;
    self.accessibilityLabel = NSLocalizedString(@"Dismiss Alert", @"Dismiss Alert Close Button");
    self.accessibilityHint = NSLocalizedString(@"Dismisses this alert.",@"Dismiss Alert close button hint");
    return self;
}

- (UIImage *)closeButtonImage{
    UIGraphicsBeginImageContextWithOptions(self.bounds.size, NO, 0);
    
    //// General Declarations
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    //// Color Declarations
    UIColor *topGradient = [UIColor colorWithRed:0.21 green:0.21 blue:0.21 alpha:0.9];
    UIColor *bottomGradient = [UIColor colorWithRed:0.03 green:0.03 blue:0.03 alpha:0.9];
    
    //// Gradient Declarations
    NSArray *gradientColors = @[(id)topGradient.CGColor,
    (id)bottomGradient.CGColor];
    CGFloat gradientLocations[] = {0, 1};
    CGGradientRef gradient = CGGradientCreateWithColors(colorSpace, (__bridge CFArrayRef)gradientColors, gradientLocations);
    
    //// Shadow Declarations
    CGColorRef shadow = [UIColor blackColor].CGColor;
    CGSize shadowOffset = CGSizeMake(0, 1);
    CGFloat shadowBlurRadius = 3;
    CGColorRef shadow2 = [UIColor blackColor].CGColor;
    CGSize shadow2Offset = CGSizeMake(0, 1);
    CGFloat shadow2BlurRadius = 0;
    
    
    //// Oval Drawing
    UIBezierPath *ovalPath = [UIBezierPath bezierPathWithOvalInRect:CGRectMake(4, 3, 24, 24)];
    CGContextSaveGState(context);
    [ovalPath addClip];
    CGContextDrawLinearGradient(context, gradient, CGPointMake(16, 3), CGPointMake(16, 27), 0);
    CGContextRestoreGState(context);
    
    CGContextSaveGState(context);
    CGContextSetShadowWithColor(context, shadowOffset, shadowBlurRadius, shadow);
    [[UIColor whiteColor] setStroke];
    ovalPath.lineWidth = 2;
    [ovalPath stroke];
    CGContextRestoreGState(context);
    
    
    //// Bezier Drawing
    UIBezierPath *bezierPath = [UIBezierPath bezierPath];
    [bezierPath moveToPoint:CGPointMake(22.36, 11.46)];
    [bezierPath addLineToPoint:CGPointMake(18.83, 15)];
    [bezierPath addLineToPoint:CGPointMake(22.36, 18.54)];
    [bezierPath addLineToPoint:CGPointMake(19.54, 21.36)];
    [bezierPath addLineToPoint:CGPointMake(16, 17.83)];
    [bezierPath addLineToPoint:CGPointMake(12.46, 21.36)];
    [bezierPath addLineToPoint:CGPointMake(9.64, 18.54)];
    [bezierPath addLineToPoint:CGPointMake(13.17, 15)];
    [bezierPath addLineToPoint:CGPointMake(9.64, 11.46)];
    [bezierPath addLineToPoint:CGPointMake(12.46, 8.64)];
    [bezierPath addLineToPoint:CGPointMake(16, 12.17)];
    [bezierPath addLineToPoint:CGPointMake(19.54, 8.64)];
    [bezierPath addLineToPoint:CGPointMake(22.36, 11.46)];
    [bezierPath closePath];
    CGContextSaveGState(context);
    CGContextSetShadowWithColor(context, shadow2Offset, shadow2BlurRadius, shadow2);
    [[UIColor whiteColor] setFill];
    [bezierPath fill];
    CGContextRestoreGState(context);
    
    
    //// Cleanup
    CGGradientRelease(gradient);
    CGColorSpaceRelease(colorSpace);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return image;
}

@end

#pragma mark - UIView + Screenshot

@implementation UIView (Screenshot)

- (UIImage*)screenshot {
    UIGraphicsBeginImageContext(self.bounds.size);
    if( [self respondsToSelector:@selector(drawViewHierarchyInRect:afterScreenUpdates:)] ){
        [self drawViewHierarchyInRect:self.bounds afterScreenUpdates:YES];
    }else{
        [self.layer renderInContext:UIGraphicsGetCurrentContext()];
    }
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    // hack, helps w/ our colors when blurring
    NSData *imageData = UIImageJPEGRepresentation(image, 1); // convert to jpeg
    image = [UIImage imageWithData:imageData];
    
    return image;
}

@end

#pragma mark - UIImage + Blur

@implementation UIImage (Blur)

-(UIImage *)boxblurImageWithBlur:(CGFloat)blur {
    if (blur < 0.f || blur > 1.f) {
        blur = 0.5f;
    }
    int boxSize = (int)(blur * 40);
    boxSize = boxSize - (boxSize % 2) + 1;
    
    CGImageRef img = self.CGImage;
    
    vImage_Buffer inBuffer, outBuffer;
    
    vImage_Error error;
    
    void *pixelBuffer;
    
    
    //create vImage_Buffer with data from CGImageRef
    
    CGDataProviderRef inProvider = CGImageGetDataProvider(img);
    CFDataRef inBitmapData = CGDataProviderCopyData(inProvider);

    
    inBuffer.width = CGImageGetWidth(img);
    inBuffer.height = CGImageGetHeight(img);
    inBuffer.rowBytes = CGImageGetBytesPerRow(img);
    
    inBuffer.data = (void*)CFDataGetBytePtr(inBitmapData);
    
    //create vImage_Buffer for output
    
    pixelBuffer = malloc(CGImageGetBytesPerRow(img) * CGImageGetHeight(img));
    
    outBuffer.data = pixelBuffer;
    outBuffer.width = CGImageGetWidth(img);
    outBuffer.height = CGImageGetHeight(img);
    outBuffer.rowBytes = CGImageGetBytesPerRow(img);
    
    // Create a third buffer for intermediate processing
    void *pixelBuffer2 = malloc(CGImageGetBytesPerRow(img) * CGImageGetHeight(img));
    vImage_Buffer outBuffer2;
    outBuffer2.data = pixelBuffer2;
    outBuffer2.width = CGImageGetWidth(img);
    outBuffer2.height = CGImageGetHeight(img);
    outBuffer2.rowBytes = CGImageGetBytesPerRow(img);
    
    //perform convolution
    error = vImageBoxConvolve_ARGB8888(&inBuffer, &outBuffer2, NULL, 0, 0, boxSize, boxSize, NULL, kvImageEdgeExtend);
    error = vImageBoxConvolve_ARGB8888(&outBuffer2, &inBuffer, NULL, 0, 0, boxSize, boxSize, NULL, kvImageEdgeExtend);
    error = vImageBoxConvolve_ARGB8888(&inBuffer, &outBuffer, NULL, 0, 0, boxSize, boxSize, NULL, kvImageEdgeExtend);

    if (error) {
    }
    
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGContextRef ctx = CGBitmapContextCreate(outBuffer.data,
                                             outBuffer.width,
                                             outBuffer.height,
                                             8,
                                             outBuffer.rowBytes,
                                             colorSpace,
                                             kCGImageAlphaNoneSkipLast);
    CGImageRef imageRef = CGBitmapContextCreateImage (ctx);
    UIImage *returnImage = [UIImage imageWithCGImage:imageRef];
    
    //clean up
    CGContextRelease(ctx);
    CGColorSpaceRelease(colorSpace);
    free(pixelBuffer2);
    free(pixelBuffer);
    CFRelease(inBitmapData);
    
    CGImageRelease(imageRef);
    
    return returnImage;
}

@end
