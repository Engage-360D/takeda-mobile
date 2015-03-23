//
//  UITextFieldAutocompl.m
//  iMedicum
//
//  Created by Alexander Rudenko on 23.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "UITextFieldAutocompl.h"

@implementation UITextFieldAutocompl

@synthesize items;
@synthesize itemsSelector;

-(void)awakeFromNib{
    
    [self addTarget:self action:@selector(textChanged) forControlEvents:UIControlEventEditingChanged];
    [self addTarget:self action:@selector(textChanged) forControlEvents:UIControlEventEditingDidBegin];
    [self addTarget:self action:@selector(endEditing) forControlEvents:UIControlEventEditingDidEnd];
    self.clearButtonMode = UITextFieldViewModeWhileEditing;
        
    items = [NSMutableArray new];
    
}

-(void)setupChangeBlock:(void (^)(NSString*))ResultBlock{
    self.changeBlock = ResultBlock;
}

-(void)setupResBlock:(void (^)(int))ResultBlock{
    self.resultBlock = ResultBlock;
}

-(void)setupFinishBlock:(void (^)(id))FinishBlock{
    self.finishBlock = FinishBlock;
}


-(void)textChanged{
    [self resetSelecteds];
    if ([self checkToSameText]){
        [self selectItem:[self indexOfTyped]];
    }
    if (self.changeBlock) self.changeBlock(self.text);
    [self showItemsTable];
}

-(void)endEditing{
    [self hideItemsTable];
    if ([self checkToSameText]){
        [self selectItem:[self indexOfTyped]];
    }
    if (self.finishBlock){
        self.finishBlock(self);
    }
}

-(void)removeTap{
    appDelegate.tapToHide.enabled = NO;
}

-(void)showItemsTable{
    
    if (items.count==0){
        [self hideItemsTable];
        return;
    }
    
    // appDelegate.tapToHide.enabled = NO;

    NLDropDownType drType;
    
    id toShowing;
    if (_nLDropDownType){
        drType = [_nLDropDownType intValue];
    } else {
        drType = nD2;
    }
    
    switch (drType) {
        case nNorm:{
            toShowing = [items valueForKey:self.itemsKey];
            
            break;
        }
        case nD1:{
            toShowing = items;
            break;
        }
        case nD5:{
            toShowing = items;
            break;
        }
        case nD2:{
            toShowing = [items valueForKey:self.itemsKey];
            break;
        }
        default:
            break;
    }
    

    if(itemsSelector == nil) {
        CGFloat f = IS_IPHONE_4_INCH?150:110;
        itemsSelector = [[NIDropDown alloc] init];
        itemsSelector.delegate = self;
      //  itemsSelector.showClearBtn = self.showClearBtn;
        if (self.delegateClass) {
            itemsSelector.delegateClass = self.delegateClass;
        } else {
            itemsSelector.delegateClass = (UIViewController*)self.delegate;        }
      

        [itemsSelector showDropDown:self :&f :toShowing :nil :@"down"];
        itemsSelector.nLDropDownType = drType;
        if (self.descriptions) itemsSelector.descriptionsList = self.descriptions;
        itemsSelector.sender = self;
        [itemsSelector setupResBlock: ^(int index){
            
            if (self.resultBlock) {
                self.resultBlock(index);
            }
            
            [self selectedItemFromList:index];
        }];
    }
    else {
        
        itemsSelector.list = toShowing;
        [itemsSelector.table reloadData];
    }
    
}

-(void)updateItemsTable:(NSArray*)Items{
    self.descriptions = nil;
    self.items = Items;
    if ([self checkToSameText]){
        [self selectItem:[self indexOfTyped]];
    }
    [self showItemsTable];
}

-(void)updateItemsTable:(NSArray*)Items descriptions:(NSMutableArray*)descriptions{
    self.items = Items;
    self.descriptions = descriptions;
    if ([self checkToSameText]){
        [self selectItem:[self indexOfTyped]];
    }
    [self showItemsTable];
}

-(void)selectedItemFromList:(int)index{
    [self selectItem:index];
    if (self.isFirstResponder) [self resignFirstResponder];
}

-(NSInteger)indexOfTyped{
    NSArray *list = [items valueForKey:self.itemsKey];
    return [list indexOfCaseInsensitiveString:self.text];
}


- (void) niDropDownDelegateMethod: (NIDropDown *) sender {
 //   appDelegate.tapToHide.enabled = YES;
    [self rel:sender];
}

-(void)niDropDownClearMethod:(NIDropDown *)sender{
}

-(void)rel:(NIDropDown *) dropDown{
    itemsSelector = nil;
}


-(void)hideItemsTable{
    [itemsSelector hideDropDown:self];
    [self rel:itemsSelector];
}

-(BOOL)checkToSameText{
    if (items.count >0){
        if ([self indexOfTyped]!=NSNotFound){
        return YES;
        }
    }
    return NO;
}

-(void)selectItem:(int)index{
    self.lastSelectedItem = self.selectedItem;
    self.selectedItem = items[index];
    self.selectedValue = [self.selectedItem valueForKey:self.itemsKey];
    self.text = self.selectedValue;
    if (self.itemsIdKey) self.selectedId = [self.selectedItem valueForKey:self.itemsIdKey];
}

-(void)resetSelecteds{
    self.selectedItem = nil;
    self.selectedValue = nil;
    self.selectedId = nil;
}

-(BOOL)selectedIsChanged{
    return self.lastSelectedItem != self.selectedItem;
}


@end

