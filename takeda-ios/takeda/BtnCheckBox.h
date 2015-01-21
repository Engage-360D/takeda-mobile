//
//  BtnCheckBox.h
//  mmim
//
//  Created by SashOK on 8/9/14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BtnCheckBox : UIButton

#define ch1 @"checkButtonChecked"
#define ch2 @"hideLogo"
#define ch3 @""
#define ch4 @""

#define unCh1 @"checkButtonUnchecked"
#define unCh2 @"showLogo"
#define unCh3 @""
#define unCh4 @""

#define h1 10
#define h2 15
#define h3 20
#define h4 25


@property (nonatomic, strong) UIImage *checkedImage;
@property (nonatomic, strong) UIImage *unCheckedImage;
@property (nonatomic, strong) NSString *text;
@property (nonatomic, strong) UIFont *text_font;
@property (nonatomic, strong) UIColor *text_color;
@property (nonatomic) float textPadding;
@property (nonatomic) float imageHeight;
@property (nonatomic, strong) id target;
@property (nonatomic, strong) NSIndexPath *indexPath;

@property (nonatomic, strong) void (^resultBlock) (bool,id);

-(id)initWithFrame:(CGRect)frame text:(NSString*)text uncheckedImage:(NSString*)unCheckedImage checkedImage:(NSString*)checkedImage ResultBlock:(void (^)(bool,id))ResultBlock;
-(void)setupText:(NSString*)text uncheckedImage:(NSString*)unCheckedImage checkedImage:(NSString*)checkedImage ResultBlock:(void (^)(bool,id))ResultBlock;
-(void)setupCheckBox;
-(void)changeButtonState:(bool)state;

@end
