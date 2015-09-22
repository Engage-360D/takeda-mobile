//
//  AnalizDataUserPage.m
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//
#import "RNBlurModalView.h"

#import "AnalizDataUserPage.h"
#import "analizRadioCell.h"
#import "analizEasyCell.h"
#import "analizeCheckCell.h"
#import "buttonWithID.h"
#import "analizData.h"


@interface AnalizDataUserPage (){
    TTTAttributedLabel *tlsubtitle;
}

@end

@implementation AnalizDataUserPage
@synthesize nextStepBtn;
@synthesize sourceData;
@synthesize tableView;
@synthesize page;
@synthesize delegate;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.nextStepBtn setBackgroundImage:[[UIImage imageNamed:@"button_arrow_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 30)] forState:UIControlStateNormal];
    self.nextStepBtn.titleLabel.font = [UIFont fontWithName:@"SegoeWP Light" size:17.0];
    self.tableView.tableFooterView = self.tableView.separ;
    NSString *subText = @"на основании шкал SCORE и PROCAM";
    self.vcSubTitle.text = subText;
    NSRange scoreRange = [subText rangeOfString:@"SCORE"];
    NSRange procamRange = [subText rangeOfString:@"PROCAM"];
    tlsubtitle = (TTTAttributedLabel*)self.vcSubTitle;
    tlsubtitle.delegate = self;
    tlsubtitle.linkAttributes = @{ (id)kCTForegroundColorAttributeName: [UIColor whiteColor], NSUnderlineStyleAttributeName: [NSNumber numberWithInt:NSUnderlineStyleSingle] };
    [tlsubtitle addLinkToURL:[NSURL URLWithString:kScoreLink] withRange:scoreRange];
    [tlsubtitle addLinkToURL:[NSURL URLWithString:kProcamLink] withRange:procamRange];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];

}

-(void)reloadData{
    [self reloadDataSource];
    [self.tableView reloadData];
}

-(IBAction)showPageInfo:(id)sender{
   // [self showMessage:@"" title:@""];
}

#pragma mark - TTTAttributedLabelDelegate

- (void)attributedLabel:(__unused TTTAttributedLabel *)label
   didSelectLinkWithURL:(NSURL *)url {
    [[UIApplication sharedApplication] openURL:url];
}

- (void)attributedLabel:(__unused TTTAttributedLabel *)label didLongPressLinkWithURL:(__unused NSURL *)url atPoint:(__unused CGPoint)point {
    
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [sourceData count];
}

 - (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
 {
     int type = [[[sourceData objectAtIndex:indexPath.row] objectForKey:@"type"] intValue];
     
     
     switch (type) {
         case 1:{
             static NSString *CellIdentifier = @"analizRadioCell";
             analizRadioCell *cell = nil;
             cell = (analizRadioCell *) [self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
             
             if(!cell)
             {
                 NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"analizRadioCell" owner:nil options:nil];
                 for(id currentObject in topLevelObjects)
                 {
                     if([currentObject isKindOfClass:[analizRadioCell class]])
                     {
                         cell = (analizRadioCell *)currentObject;
                         break;
                     }
                 }
             }
             cell.name.text = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"name"];
             cell.name.font = [self getFontName];
             cell.first_param.font = [self getFontRadioItem];
             cell.second_param.font = [self getFontRadioItem];
             
             [cell.first_item addTarget:self action:@selector(selectItem:) forControlEvents:UIControlEventTouchDown];
             [cell.second_item addTarget:self action:@selector(selectItem:) forControlEvents:UIControlEventTouchDown];
             
             
             cell.first_item.type_object = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"object"];
             cell.second_item.type_object = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"object"];
             
             int value = [[[sourceData objectAtIndex:indexPath.row] objectForKey:@"value"] intValue];
             
             if (value == 0) {
                 [cell.first_item setImage:[UIImage imageNamed:@"pageIndicator_enable"] forState:UIControlStateNormal];
                 [cell.second_item setImage:[UIImage imageNamed:@"pageIndicator_disable"] forState:UIControlStateNormal];
             }else{
                 [cell.first_item setImage:[UIImage imageNamed:@"pageIndicator_disable"] forState:UIControlStateNormal];
                 [cell.second_item setImage:[UIImage imageNamed:@"pageIndicator_enable"] forState:UIControlStateNormal];
             }
             
             
             
             UIView *sel_view = [[UIView alloc] init];
             sel_view.backgroundColor = [UIColor clearColor];
             cell.selectedBackgroundView = sel_view;
             return cell;
             break;}
         case 2:{
             static NSString *CellIdentifier = @"analizEasyCell";
             analizEasyCell *cell = nil;
             cell = (analizEasyCell *) [self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
             
             BOOL fractions = NO;
             if ([[sourceData objectAtIndex:indexPath.row] objectForKey:@"fractions"]) {
                 fractions = YES;
             }
             if(!cell)
             {
                 
                 NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:(fractions)?@"analizEasyDrobCell":@"analizEasyCell" owner:nil options:nil];
                 for(id currentObject in topLevelObjects)
                 {
                     if([currentObject isKindOfClass:[analizEasyCell class]])
                     {
                         cell = (analizEasyCell *)currentObject;
                         break;
                     }
                 }
             }
             cell.name.text = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"name"];
             cell.name.font = [self getFontName];
             cell.description.text = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"description"];
             
             
             if (fractions) {
                 NSArray *objects = [[[sourceData objectAtIndex:indexPath.row] objectForKey:@"description"] componentsSeparatedByString:@"/"];
                 if ([objects count]==2) {
                     cell.description_1.text = objects[0];
                     cell.description_2.text = objects[1];
                     
                     cell.description_1.font = [self getDrobDescriptionFont];
                     cell.description_2.font = [self getDrobDescriptionFont];
                 }else{
                     cell.description_1 = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"description"];
                 }
             }
             
             
             
             
             cell.description.font = [self getFontDescription];
             cell.value.font = [self getFontValue];
             cell.value.text = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"value"];

             UIView *sel_view = [[UIView alloc] init];
             sel_view.backgroundColor = [UIColor clearColor];
             cell.selectedBackgroundView = sel_view;
             return cell;
             break;}
         case 3:{
             static NSString *CellIdentifier = @"analizeCheckCell";
             analizeCheckCell *cell = nil;
             cell = (analizeCheckCell *) [self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
             
             if(!cell)
             {
                 NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"analizeCheckCell" owner:nil options:nil];
                 for(id currentObject in topLevelObjects)
                 {
                     if([currentObject isKindOfClass:[analizeCheckCell class]])
                     {
                         cell = (analizeCheckCell *)currentObject;
                         break;
                     }
                 }
             }
             cell.name.text = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"name"];
             cell.name.font = [self getFontName];
             
             

             
             
             int value = [[[sourceData objectAtIndex:indexPath.row] objectForKey:@"value"] intValue];
             
             if (value==0) {
                 [cell.selected_item setImage:[UIImage imageNamed:@"unchecked_item.png"] forState:UIControlStateNormal];
             }else{
                 [cell.selected_item setImage:[UIImage imageNamed:@"checked_item.png"] forState:UIControlStateNormal];
             }
             
             
             [cell.selected_item addTarget:self action:@selector(selectCheckItem:) forControlEvents:UIControlEventTouchDown];
             
             
             cell.selected_item.type_object = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"object"];
             cell.selected_item.id_button = value;
            
             UIView *sel_view = [[UIView alloc] init];
             sel_view.backgroundColor = [UIColor clearColor];
             cell.selectedBackgroundView = sel_view;
             return cell;
             break;}
         default:{
             return nil;
             break;}
     }
     
     
     

 }

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    int type = [[[sourceData objectAtIndex:indexPath.row] objectForKey:@"type"] intValue];
    if (type==2) {
        if ([self.delegate respondsToSelector:@selector(analizDataUserPage:openList:sourceData:)]) {
            [self.delegate analizDataUserPage:self openList:[[sourceData objectAtIndex:indexPath.row] objectForKey:@"object"] sourceData:[sourceData objectAtIndex:indexPath.row]];
        }
    }else{
        if (type==3) {
            buttonWithID *tmp = [[buttonWithID alloc] init];
            tmp.type_object = [[sourceData objectAtIndex:indexPath.row] objectForKey:@"object"];
            tmp.id_button = [[[sourceData objectAtIndex:indexPath.row] objectForKey:@"value"] intValue];
            [self selectCheckItem:tmp];
            
        }
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    int type = [[[sourceData objectAtIndex:indexPath.row] objectForKey:@"type"] intValue];
    float width = (type==1)?62.0:(type==2)?164.0:225.0;
    float h = (ceilf([Helper heightText:[[sourceData objectAtIndex:indexPath.row] objectForKey:@"name"] withFont:[self getFontName] withWidth:width] + 20.0));
    NSLog(@"h = %f",h);
    return h;
    //return 44.0;
}








-(UIFont*)getFontName{
    return [UIFont fontWithName:@"SegoeUI-Light" size:17.0];
}
-(UIFont*)getFontDescription{
    return [UIFont fontWithName:@"SegoeUI-Light" size:15.0];
}

-(UIFont*)getDrobDescriptionFont{
    return [UIFont fontWithName:@"SegoeUI-Light" size:13.0];
}

-(UIFont*)getFontValue{
    return [UIFont fontWithName:@"SegoeUI-Light" size:22.5];
}
-(UIFont*)getFontRadioItem{
    return [UIFont fontWithName:@"SegoeUI-Light" size:15.0];
}



-(void)selectItem:(buttonWithID*)sender{
    if ([sender tag]==1) {
        [[[analizData sharedObject] dicRiskData] setObject:@"0" forKey:sender.type_object];
    }else{
        [[[analizData sharedObject] dicRiskData] setObject:@"1" forKey:sender.type_object];
    }
    [self reloadDataSource];
}


-(void)selectCheckItem:(buttonWithID*)sender{
    if (sender.id_button==0) {
        [[[analizData sharedObject] dicRiskData] setObject:@"1" forKey:sender.type_object];
    }else{
        [[[analizData sharedObject] dicRiskData] setObject:@"0" forKey:sender.type_object];
    }
    [self reloadDataSource];
    
}




-(void)reloadDataSource{
    if (page==1) {
        self.sourceData = [[analizData sharedObject] getQuestionsDataUser];
        [self checkUserDataFields];
    }else{
        if (page==2) {
            self.sourceData = [[analizData sharedObject] getQuestionsHistoryUser];
            [self checkUserHistoryFields];
        }else{
            self.sourceData = [[analizData sharedObject] getQuestionsDailyRation];
        }
    }
    [self.tableView reloadData];
}




-(void)checkUserDataFields{
    float cholesterol = 0;
    if (![[[[analizData sharedObject] dicRiskData] objectForKey:@"cholesterol"] isEqualToString:@"-"]) {
        cholesterol = [[[[analizData sharedObject] dicRiskData] objectForKey:@"cholesterol"] floatValue];
    }
    if (cholesterol <= 4.9) {
        [[[analizData sharedObject] dicRiskData] setObject:@"-" forKey:@"drags_cholesterol"];
    } else {
        [[[analizData sharedObject] dicRiskData] setObject:[NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"drags_cholesterol"] boolValue]] forKey:@"drags_cholesterol"];
    }
    if (cholesterol <= 4.9) {
        NSMutableArray *tmp = [[NSMutableArray alloc] init];
        for (int i = 0; i < [self.sourceData count]; i++) {
            if (![[[self.sourceData objectAtIndex:i] objectForKey:@"object"] isEqualToString:@"drags_cholesterol"]) {
                [tmp addObject:[self.sourceData objectAtIndex:i]];
            }
        }
        self.sourceData = tmp;
    }
}




-(void)checkUserHistoryFields{
    float arterial_pressure = 0;
    if (![[[[analizData sharedObject] dicRiskData] objectForKey:@"arterial_pressure"] isEqualToString:@"-"]) {
        arterial_pressure = [[[[analizData sharedObject] dicRiskData] objectForKey:@"arterial_pressure"] floatValue];
    }
    
    bool suffer_diabet = [[[[analizData sharedObject] dicRiskData] objectForKey:@"diabet"] boolValue];
    
    
    
    NSMutableArray *tmp = [[NSMutableArray alloc] init];
    for (int i = 0; i < [self.sourceData count]; i++) {
        bool can_add = YES;
        
        if ([[[self.sourceData objectAtIndex:i] objectForKey:@"object"] isEqualToString:@"decrease_pressure_drags"] && arterial_pressure<=139) {
            can_add = NO;
            [[[analizData sharedObject] dicRiskData] setObject:@"-" forKey:@"decrease_pressure_drags"];
        } else {
            [[[analizData sharedObject] dicRiskData] setObject:[NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"decrease_pressure_drags"] boolValue]] forKey:@"decrease_pressure_drags"];
        }
        
        if ([[[self.sourceData objectAtIndex:i] objectForKey:@"object"] isEqualToString:@"higher_suger_blood"] && suffer_diabet) {
            can_add = NO;
            [[[analizData sharedObject] dicRiskData] setObject:@"-" forKey:@"higher_suger_blood"];
        } else {
            [[[analizData sharedObject] dicRiskData] setObject:[NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"higher_suger_blood"] boolValue]] forKey:@"higher_suger_blood"];
        }
        
        if ([[[self.sourceData objectAtIndex:i] objectForKey:@"object"] isEqualToString:@"accept_drags_suger"] && !suffer_diabet) {
            can_add = NO;
            [[[analizData sharedObject] dicRiskData] setObject:@"-" forKey:@"accept_drags_suger"];
        } else {
            [[[analizData sharedObject] dicRiskData] setObject:[NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"accept_drags_suger"] boolValue]] forKey:@"accept_drags_suger"];
        }
        
        if (can_add) {
            [tmp addObject:[self.sourceData objectAtIndex:i]];
        }
        
    }
    self.sourceData = tmp;

}






@end

