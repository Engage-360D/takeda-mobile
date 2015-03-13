//
//  NIDropDown.h
//  NIDropDown
//
//  Created by Bijesh N on 12/28/12.
//  Copyright (c) 2012 Nitor Infotech. All rights reserved.
//

#import <UIKit/UIKit.h>
/// #import "ListTableView.h"

typedef enum {
    nNorm = 0,
    nD1 = 1,
    nD2 = 2,
    nD3 = 3,
    nD5 = 5,
    normal6 = 6
} NLDropDownType;



@class NIDropDown;
@protocol NIDropDownDelegate<NSObject>
- (void) niDropDownDelegateMethod: (NIDropDown *) sender;
- (void) niDropDownClearMethod: (NIDropDown *) sender;
- (void) rel:(NIDropDown *) sender;

@end

@interface NIDropDown : UIView <UITableViewDelegate, UITableViewDataSource>
{
    NSString *animationDirection;
    UIImageView *imgView;
}

@property(nonatomic, strong) UITableView *table;
@property(nonatomic, strong) UIView *btnSender;
@property(nonatomic, retain) NSArray *list;
@property(nonatomic, retain) NSArray *descriptionsList;
@property(nonatomic, retain) NSArray *imageList;

@property (nonatomic, retain) NSObject <NIDropDownDelegate> *delegate;
@property (nonatomic, retain) UIViewController *delegateClass;
@property (nonatomic, retain) NSString *animationDirection;
@property (nonatomic, retain) UIView *sender;
@property (nonatomic, strong) id targetDict;
@property (nonatomic, strong) id targetKey;
@property (nonatomic, strong) id targetSuperArray;
@property (nonatomic, strong) id targetSuperKey;
@property (nonatomic) BOOL showClearBtn;
@property (nonatomic) BOOL showed;

@property (nonatomic, strong) UIView *header;
@property (nonatomic, strong) UIButton *clearBtn;


@property (nonatomic, strong) UIFont *fontNameText;
@property (nonatomic, strong) UIColor *colorText;


@property (nonatomic) NLDropDownType nLDropDownType;
@property (nonatomic, strong) void (^resultBlock) (int);
@property(nonatomic) int selectedIndex;
@property(nonatomic) float customWidth;
@property(nonatomic) float customX;

-(void)setupResBlock:(void (^)(int))ResultBlock;

-(void)hideDropDown:(UIView *)b;
- (id) showDropDown:(UIView *)b:(CGFloat *)height:(NSArray *)arr:(NSArray *)imgArr:(NSString *)direction;
@end
