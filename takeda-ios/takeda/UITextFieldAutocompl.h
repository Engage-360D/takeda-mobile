//
//  UITextFieldAutocompl.h
//  iMedicum
//
//  Created by Alexander Rudenko on 23.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NIDropDown.h"
#import "NSArray+CaseInsensitiveIndexing.h"

@interface UITextFieldAutocompl : UITextField<NIDropDownDelegate>

@property (nonatomic, strong) NIDropDown *itemsSelector;
@property (nonatomic, retain) UIViewController *delegateClass;
@property (nonatomic, strong) NSArray *items;
@property (nonatomic, strong) NSArray *descriptions;

@property (nonatomic, strong) NSString *selectedId;
@property (nonatomic, strong) NSString *selectedValue;
@property (nonatomic, strong) id selectedItem;
@property (nonatomic, strong) NSString *itemsKey;
@property (nonatomic, strong) NSString *itemsIdKey;
@property (nonatomic, strong) id lastSelectedItem;
@property (nonatomic, strong) id info;
@property (nonatomic, strong) NSString* nLDropDownType;
@property (nonatomic) BOOL showClearBtn;


@property (nonatomic, strong) void (^changeBlock) (NSString*);
@property (nonatomic, strong) void (^resultBlock) (int);
@property (nonatomic, strong) void (^finishBlock) (id);


-(void)setupChangeBlock:(void (^)(NSString*))ResultBlock;
-(void)setupResBlock:(void (^)(int))ResultBlock;
-(void)setupFinishBlock:(void (^)(id))FinishBlock;


-(void)showItemsTable;
-(void)updateItemsTable:(NSArray*)Items;
-(void)updateItemsTable:(NSArray*)Items descriptions:(NSMutableArray*)descriptions;
-(void)selectedItemFromList:(int)index;
-(void)hideItemsTable;
-(BOOL)selectedIsChanged;


@end
