//
//  DPicker.h
//  takeda
//
//  Created by Alexander Rudenko on 06.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    dEmpty = 0,
    dList = 1,
    dDate = 2
} DPickerType;

@interface DPicker : UIView<UIPickerViewDelegate, UIPickerViewDataSource>

@property (nonatomic, strong) void (^resultListBlock) (BOOL apply, int index);
@property (nonatomic, strong) void (^resultDateBlock) (BOOL apply, NSDate *resultDate);

@property (nonatomic, strong) UIView *parent_view;
@property (nonatomic, strong) UIView *backView;
@property (nonatomic, strong) UITapGestureRecognizer *tap;
@property (nonatomic, strong) UIPickerView *list_picker;
@property (nonatomic, strong) UIDatePicker *date_picker;
@property (nonatomic, strong) UIToolbar *toolbar;

@property (nonatomic, strong) NSArray *listArray;
@property (nonatomic) int selectedIndex;
@property (nonatomic) DPickerType pickerType;

-(id)initListWithArray:(NSArray*)list inView:(UIView*)the_view completition:(void (^)(BOOL apply, int index))ResultBlock;
-(id)initDateinView:(UIView*)the_view completition:(void (^)(BOOL apply, NSDate *resultDate))ResultBlock;

-(void)selectIndex:(int)index;
-(void)preselectValue:(id)preValue;

-(void)show;
-(void)dissmiss;
-(void)closePicker;
-(void)applyPicker;

-(UIToolbar*)toolbar;

@end
