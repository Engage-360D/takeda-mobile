//
//  LPUDetail.m
//  takeda
//
//  Created by Alexander Rudenko on 20.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "LPUDetail.h"

@interface LPUDetail (){
    NSMutableArray *menu_data;
}

@end

@implementation LPUDetail
@synthesize lpu;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
    [self showInfo];
    [self setupFields];
}

-(void)initData{
    menu_data = [Global recursiveMutable:@[@[@{@"title":@"Телефон",@"value":@"+7(495)111-111"},@{@"title":@"Регистратура",@"value":@"+7(495)111-112"}],
                                           @[@{@"title":@"Отделение лечения сердечно-сосудистых заболеваний",@"value":@"+7(495)111-113"}],
                                           @[@{@"title":@"Специалист",@"value":@"Иванов Иван Иванович"}],
                                           @[@{@"title":@"Часы работы",@"value":@"8:00 - 22:00"}],

                                           ]];
}

-(void)setupInterface{
    //SegoeWP Semibold
    self.lpuName.font = [UIFont fontWithName:@"SegoeWP-Semibold" size:17];
    self.lpuAddress.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    self.lpuInfoText.font = [UIFont fontWithName:@"SegoeWP" size:14];

}

-(void)showInfo{
    self.lpuName.text = lpu[@"name"];
    self.lpuAddress.text = lpu[@"address"];
 //   [self drawBlocks];
}

-(void)setupFields{
    self.lpuName.height = [Global heightLabel:self.lpuName];
    self.lpuAddress.height = [Global heightLabel:self.lpuAddress];
    self.lpuAddress.y = self.lpuName.bottom+10;
    self.fieldsContainer.y = self.lpuAddress.bottom+20;
    self.lpuNameAdressBack.height = self.fieldsContainer.y;

    [self.fieldsContainer arrangeViewsVertically];
    [self.fieldsContainer setupAutosizeBySubviews];
    [self.scrollView setup_autosize];

}

-(void)drawBlocks{
    [self.fieldsContainer removeSubviews];
    for (int i = 0; i<menu_data.count; i++){
        UIView *block = [self keyValuesBlock:menu_data[i] width:self.fieldsContainer.width padding:0 interval:-10];
        [self.fieldsContainer addSubview:block];
    }
    [self.fieldsContainer addSubview:self.lpuInfoContainer];
}

-(UIView*)keyValuesBlock:(NSMutableArray*)ds width:(float)width padding:(float)padding interval:(float)interval{
    UIView *container = [[UIView alloc] initWithFrame:CGRectMake(0, 0, width, 0)];
    float posX = padding;
    for (int i = 0; i<ds.count; i++){
        UITextView *tf = [self keyValueRecord:ds[i][@"title"] value:ds[i][@"value"] width:self.fieldsContainer.width];
        [container addSubview:tf];
        tf.y = posX;
        posX += tf.height;
        posX += interval;
    }
    
    container.height = posX + padding - interval+3;
    [container addSeparator];
    
    return container;
}

-(UITextView*)keyValueRecord:(NSString*)theKey value:(NSString*)theValue width:(float)width{
    NSString *fullString = [NSString stringWithFormat:@"%@: %@",theKey,theValue];
    UITextView *tf = [[UITextView alloc] initWithFrame:CGRectMake(10, 0, width, 0)];
    tf.scrollEnabled = NO;
    tf.editable = NO;
    tf.selectable = YES;
    tf.dataDetectorTypes = UIDataDetectorTypePhoneNumber;
    tf.contentInset = UIEdgeInsetsMake(0, 0, 0, 0);
    tf.backgroundColor = [UIColor clearColor];
    NSMutableAttributedString *attrStr = [[NSMutableAttributedString alloc] initWithString:fullString];
    UIFont *keyFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    UIFont *valueFont = [UIFont fontWithName:@"SegoeWP-Light" size:14.0];

    UIColor *keyColor = RGB(54, 65, 71);
    UIColor *valueColor = RGB(54, 65, 71);

    [attrStr addAttribute:NSFontAttributeName value:keyFont range:NSMakeRange(0, theKey.length+1)];
    [attrStr addAttribute:NSForegroundColorAttributeName value:keyColor range:NSMakeRange(0, theKey.length+1)];
    [attrStr addAttribute:NSFontAttributeName value:valueFont range:NSMakeRange(theKey.length+1, theValue.length+1)];
    [attrStr addAttribute:NSForegroundColorAttributeName value:valueColor range:NSMakeRange(theKey.length+1, theValue.length+1)];

    tf.attributedText = attrStr;
    tf.height = [Global textViewHeightForAttributedText:attrStr andWidth:width];
    
    return tf;
}



@end
