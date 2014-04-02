//
//  AnalizDataUserPage.m
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "AnalizDataUserPage.h"
#import "analizRadioCell.h"
#import "analizEasyCell.h"
#import "analizeCheckCell.h"


@interface AnalizDataUserPage ()

@end

@implementation AnalizDataUserPage
@synthesize nextStepBtn;
@synthesize titleRisk;
@synthesize sourceData;
@synthesize tableView;

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
}





-(void)reloadData{
    [self.tableView reloadData];
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
             
             
             if (indexPath.row == 0) {
                 cell.top_separator.hidden = NO;
             }else{
                 cell.top_separator.hidden = YES;
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
             
             if(!cell)
             {
                 NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"analizEasyCell" owner:nil options:nil];
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
             
             cell.description.font = [self getFontDescription];
             cell.value.font = [self getFontValue];
             
             
             if (indexPath.row == 0) {
                 cell.top_separator.hidden = NO;
             }else{
                 cell.top_separator.hidden = YES;
             }
             
             
             
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
             
             
             
             
             
             if (indexPath.row == 0) {
                 cell.top_separator.hidden = NO;
             }else{
                 cell.top_separator.hidden = YES;
             }
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
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    int type = [[[sourceData objectAtIndex:indexPath.row] objectForKey:@"type"] intValue];
    float width = (type==1)?62.0:(type==2)?164.0:225.0;
    
    
    return (ceilf([Helper heightText:[[sourceData objectAtIndex:indexPath.row] objectForKey:@"name"] withFont:[self getFontName] withWidth:width] + 20.0));
    //return 44.0;
}



-(UIFont*)getFontName{
    return [UIFont fontWithName:@"SegoeUI-Light" size:17.0];
}
-(UIFont*)getFontDescription{
    return [UIFont fontWithName:@"SegoeUI-Light" size:15.0];
}
-(UIFont*)getFontValue{
    return [UIFont fontWithName:@"SegoeUI-Light" size:22.5];
}
-(UIFont*)getFontRadioItem{
    return [UIFont fontWithName:@"SegoeUI-Light" size:15.0];
}


@end
