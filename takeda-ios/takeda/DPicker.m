//
//  DPicker.m
//  takeda
//
//  Created by Alexander Rudenko on 06.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "DPicker.h"
#import "UIView+ViewsParentUIViewController.h"


#define hhh 250

@implementation DPicker{
    
}

@synthesize list_picker;
@synthesize date_picker;
@synthesize listArray;
@synthesize selectedIndex;
@synthesize applyBtn;
@synthesize closeBtn;

//@property (nonatomic, strong) void (^simpleResBlock) (void);

-(id)initListWithArray:(NSArray*)list inView:(UIView*)the_view completition:(void (^)(BOOL apply, int index))ResultBlock{
    self = [super init];
    self.pickerType = dList;
    self.resultListBlock = ResultBlock;
    self.listArray = list;
    self.tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dissmiss)];
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, the_view.width, the_view.height)];
    [self.backView addGestureRecognizer:self.tap];
    self.backView.backgroundColor = [UIColor colorWithWhite:0.5 alpha:0.5];
    self.backgroundColor = [UIColor whiteColor];
    self.parent_view = the_view;
    self.height = hhh;
    self.frame = CGRectMake(0, the_view.height, the_view.width, self.height);
    
    list_picker = [[UIPickerView alloc] initWithFrame:CGRectMake(0,40,the_view.width,210)];
    list_picker.delegate = self;
    list_picker.dataSource = self;
    list_picker.showsSelectionIndicator = YES;
    
    [self addSubview:self.toolbar];
    [self addSubview:list_picker];
    

 //   [list_picker selectRow:sel_index_region inComponent:0 animated:NO];
    return self;
}

-(id)initDateinView:(UIView*)the_view completition:(void (^)(BOOL apply, NSDate *resultDate))ResultBlock{
    self = [super init];
    self.pickerType = dDate;
    self.resultDateBlock = ResultBlock;
    self.tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dissmiss)];
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, the_view.width, the_view.height)];
    [self.backView addGestureRecognizer:self.tap];
    self.backView.backgroundColor = [UIColor colorWithWhite:0.5 alpha:0.5];
    self.backgroundColor = [UIColor whiteColor];
    self.parent_view = the_view;
    self.height = hhh;
    self.frame = CGRectMake(0, the_view.height, the_view.width, self.height);
    
    date_picker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0,40,the_view.width,210)];
    date_picker.datePickerMode = UIDatePickerModeDate;
    [self addSubview:self.toolbar];
    [self addSubview:date_picker];
    
    return self;
}

-(void)selectIndex:(int)index{
    selectedIndex = index;
    [list_picker selectRow:selectedIndex inComponent:0 animated:NO];
}

-(void)preselectValue:(id)preValue{
    switch (self.pickerType) {
        case dList:{

            int index = [Global index:preValue inArray:self.listArray];
            if (index!=NSNotFound){
                selectedIndex = index;
            }
            [list_picker selectRow:selectedIndex inComponent:0 animated:NO];

            break;
        }
            
        case dDate:{
            if ([preValue isKindOfClass:[NSDate class]]){
                [date_picker setDate:preValue];
            }

            break;
        }
        case dEmpty:{
            
            break;
        }
            
    }
}

-(void)show{
    if (self.parent_view){
        UIViewController * parentController = [self.parent_view firstAvailableUIViewController];
        if ([parentController respondsToSelector:@selector(hideKeyb)]){
            [parentController performSelector:@selector(hideKeyb)];
        }
    }
    [self.parent_view addSubview:self.backView];
    self.y = self.parent_view.height;
    
    [self.parent_view addSubview:self];
    [UIView animateWithDuration: 0.3f delay:0.0f options:UIViewAnimationOptionCurveLinear animations:^{
        self.y = self.parent_view.height-self.height;
        self.alpha = 1.0f;
    } completion:^(BOOL finished){
        
    }];
}

-(void)dissmiss{
    [self.backView removeFromSuperview];
    [UIView animateWithDuration: 0.3f delay:0.0f options:UIViewAnimationOptionCurveLinear animations:^{
        self.y = self.parent_view.height;
        self.alpha = 0.0f;
    } completion:^(BOOL finished){
        [self removeFromSuperview];
    }];
}

-(UIToolbar*)toolbar{
    if (!_toolbar){
        _toolbar = [[UIToolbar alloc] init];
        _toolbar.frame = CGRectMake(0, 0, ScreenWidth, 44);
        NSMutableArray *items = [[NSMutableArray alloc] init];
        
        closeBtn = [[UIBarButtonItem alloc] initWithTitle:@"Закрыть" style:UIBarButtonItemStyleDone target:self action:@selector(closePicker)];
        applyBtn = [[UIBarButtonItem alloc] initWithTitle:@"Применить" style:UIBarButtonItemStyleDone target:self action:@selector(applyPicker)];
        UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
        
        [items addObject:closeBtn];
        [items addObject:flexibleSpaceLeft];
        [items addObject:applyBtn];
        [_toolbar setItems:items animated:NO];
    }
    return _toolbar;
}

-(void)closePicker{
    [self dissmiss];
}

-(void)applyPicker{
    switch (self.pickerType) {
        case dList:{
            if (self.resultListBlock){
                self.resultListBlock(YES, selectedIndex);
            }
            break;
        }
        case dDate:{
            if (self.resultDateBlock){
                self.resultDateBlock(YES, self.date_picker.date);
            }

            break;
        }
        case dEmpty:{
            
            break;
        }
    }
    
    [self dissmiss];
}

#pragma mark PickerView DataSource

- (NSInteger)numberOfComponentsInPickerView:
(UIPickerView *)pickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView
numberOfRowsInComponent:(NSInteger)component
{
    return listArray.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    NSMutableString *titleText = [NSMutableString new];
    [titleText appendFormat:@"%@",listArray[row]];
    
    
    if (self.unitCaption.length>0){
        [titleText appendFormat:@" %@",self.unitCaption];
    }
    
    return titleText;
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row
      inComponent:(NSInteger)component{
    selectedIndex = (int)row;
}

#pragma mark -


@end
