//
//  VControllerExt.h
//  takeda
//
//  Created by Alexander Rudenko on 12.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VControllerExt : UIViewController
@property (nonatomic,retain) IBOutletCollection(UILabel) NSArray *blockLabels;
@property (nonatomic,retain) IBOutletCollection(UIView) NSArray *bg_block;
@property (nonatomic, strong) IBOutletCollection(UIImageView) NSArray *separators_collection;
@property(nonatomic, strong) IBOutlet UILabel *vcTitle;
@property(nonatomic, strong) IBOutlet UILabel *vcSubTitle;
@property (nonatomic,retain) IBOutlet UILabel *danger_text;
@property (nonatomic,retain) IBOutlet UIView *titleContainer;
@property (nonatomic,retain) IBOutlet UIView *mainElement;
@property(nonatomic) BOOL isFromMenu;
@property(nonatomic) BOOL isAppearFromBack;
@property (nonatomic,assign) VControllerExt *parentVC;
- (BOOL)isRootVC;

-(void)drawBorders:(NSArray*)array;
-(void)drawBordersInView:(UIView*)view;
-(void)setNavigationPanel;
-(void)backAction;
-(void)openLeftMenu;



-(UIImageView*)separatorLine;
-(UIBarButtonItem*)menuButton;
-(UIBarButtonItem*)personalButton;
-(UIBarButtonItem*)highlightedPersonalButton;
-(UIBarButtonItem*)alarmButton;
-(UIBarButtonItem*)backBtn;

- (UIBarButtonItem*)menuBarBtnWithImageName:(NSString*)imageName selector:(SEL)aSelector forTarget:(id)target;
- (UIBarButtonItem*)menuBarBtnWithTitle:(NSString*)titleString selector:(SEL)aSelector forTarget:(id)target;

@end
